/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
public class RadnaDretva extends Thread {

    private Socket s;
    long vrijemeZavrsetja;
    public long vrijemeIzvršavanja;
    public List<RadnaDretva> listaSvihRadnihDretva; 
    Konfiguracija konfig;
    InputStream is = null;
    OutputStream os = null;
    boolean cekanje = false;
    boolean ispis = true;
    //TODO varijabla za vrijeme početka rada dretve

    RadnaDretva(Socket socket, Konfiguracija konfig) {
        this.s = socket;
        this.konfig = konfig;
    }

    @Override
    public void interrupt() {
        vrijemeZavrsetja = System.currentTimeMillis();
        long ukupno_vrijeme = (vrijemeZavrsetja-vrijemeIzvršavanja);
        Evidencija.ukupnoTrajanjeRadnihDretva= Evidencija.ukupnoTrajanjeRadnihDretva + ukupno_vrijeme;
        Evidencija.brojPrekinutihZahtjeva++;
        System.out.println("TRAJANJE: "+ Evidencija.ukupnoTrajanjeRadnihDretva);
        if (this.cekanje == true) {
            try {
                is = s.getInputStream();
                os = s.getOutputStream();
                os.write("ERROR 13;Dretva nije uspjesno cekala".getBytes());
                os.flush();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.ispis = false;
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        long vrijemePocetka = System.currentTimeMillis();
        System.out.println(this.getClass());

        String sintaksa_admin = "^USER ([^\\s]+); PASSWD ([^\\s]+); (PAUSE|STOP|START|STAT);$";
        String sintaksa_adminPause = "^USER ([^\\s]+); PASSWD ([^\\s]+); PAUSE;$";
        String sintaksa_adminStop = "^USER ([^\\s]+); PASSWD ([^\\s]+); STOP;$";
        String sintaksa_adminStart = "^USER ([^\\s]+); PASSWD ([^\\s]+); START;$";
        String sintaksa_adminStat = "^USER ([^\\s]+); PASSWD ([^\\s]+); STAT;$";
        String sintaksa_korisnik_1 = "USER ([^\\s]+); ADD ([^\\s]+);";
        String sintaksa_korisnik_2 = "USER ([^\\s]+); TEST ([^\\s]+);";
        String sintaksa_korisnik_3 = "USER ([^\\s]+); WAIT ([^\\s]+);";

        this.vrijemeIzvršavanja = System.currentTimeMillis();
        SerijalizatorEvidencije novi = new SerijalizatorEvidencije(konfig);
        novi.start();
        try {

            is = s.getInputStream();
            os = s.getOutputStream();

            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            System.out.println("Primljena naredba: " + sb);

            //TODO provjeri ispravnost pripremljenog zahtjeva
            Pattern p = Pattern.compile(sintaksa_admin);
            Matcher m = p.matcher(sb);
            boolean status = m.matches();
            System.out.println("ADMIN pause : " + status);
            if (status) {
                String line;
                try (
                        InputStream fis = new FileInputStream(konfig.dajPostavku("adminDatoteka"));
                        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                        BufferedReader br = new BufferedReader(isr);) {
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        String[] parts = line.split(";");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1]; // 034556
                        if (part1.equals(KorisnikSustava.username) && (part2.equals(KorisnikSustava.password))) {
                            System.out.println("DOSAO SAM OVDJE");
                            os.write("OK".getBytes());
                            os.flush();
                        } else {

                        }
                    }
                }
                System.out.println("admin");
                p = Pattern.compile(sintaksa_adminPause);
                m = p.matcher(sb);
                status = m.matches();
                System.out.println("koja opcija: "+ status);
                if (status) {
                    os.write("Odabrali ste opciju pause".getBytes());
                    os.flush();
                    ServerPause();
                }
                p = Pattern.compile(sintaksa_adminStart);
                m = p.matcher(sb);
                status = m.matches();
                if (status) {
                    adminStart();
                }
                p = Pattern.compile(sintaksa_adminStop);
                m = p.matcher(sb);
                status = m.matches();
                if (status) {
                    adminStop();
                }
                p = Pattern.compile(sintaksa_adminStat);
                m = p.matcher(sb);
                status = m.matches();
                if (status) {

                }
                //TODO dobršiti za admina
            } else {
                p = Pattern.compile(sintaksa_korisnik_1);
                m = p.matcher(sb);
                status = m.matches();
                if (status) {
                    if(ServerSustava.serverPauziran){
                    os.write("ERROR; Server je pauziran!".getBytes());
                    os.flush();
                    return;
                    }
                      userAdresaAdd(m.group(2));

                } else {
                    p = Pattern.compile(sintaksa_korisnik_2);
                    m = p.matcher(sb);
                    status = m.matches();
                    if (status) {
                        if(ServerSustava.serverPauziran){
                         os.write("ERROR; Server je pauziran!".getBytes());
                         os.flush();
                         return;
                    }
                        userAdresaTest(m.group(2));
                    } else {
                        //TODO i tako za sve ostale slučajve
                        if (status) {
                            //TODO dovršiti za korisnika 1. slučaj
                        } else {
                            p = Pattern.compile(sintaksa_korisnik_3);
                            m = p.matcher(sb);
                            status = m.matches();
                            if (status) {
                                if(ServerSustava.serverPauziran){
                                os.write("ERROR; Server je pauziran!".getBytes());
                    os.flush();
                    return;
                    }
                                userWait(m.group(2));
                            } else {
                                System.out.println("Krivi regex kralju");
                            }
                        }

                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //TODO obrisati dretvu iz kolekcije aktivnih radnih dretvi
        //TODO smanjiti brojač aktivnih radnih dretvi
        //TODO ažurirati evidenciju rada
    }

    public void adminStop() {
        for (RadnaDretva radnaDretva : this.listaSvihRadnihDretva) {
            radnaDretva.interrupt();
        }
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        ServerSustava.ugasiAplikaciju();
    }

    public boolean userAdresaAdd(String adresa) {
        System.out.println("OVO SAM POSLAO: " + adresa + " ovoliko ih ima u listi: " + Evidencija.sveAdrese.size());
        if (Evidencija.sveAdrese.size() == 7) {
            try {
                is = s.getInputStream();
                os = s.getOutputStream();
                os.write("ERROR 10;Nema slobodnog mjesta".getBytes());
                os.flush();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        if (Evidencija.sveAdrese.size() == 0) {
            EntitetAdrese novi = new EntitetAdrese(adresa);
            novi.brojZahtjeva = 1;
            Evidencija.zadnjaAdresa = novi;
            Evidencija.sveAdrese.add(novi);
            try {
                is = s.getInputStream();
                os = s.getOutputStream();
                os.write("OK".getBytes());
                os.flush();

            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        System.out.println("prije petlje:" + Evidencija.sveAdrese);
        for (EntitetAdrese entitetAdrese : new ArrayList<EntitetAdrese>(Evidencija.sveAdrese)) {

            System.out.println("istina:" + entitetAdrese.adresa.equals(adresa));
            if (entitetAdrese.adresa.equals(adresa)) {
                entitetAdrese.brojZahtjeva++;
                Evidencija.zadnjaAdresa = entitetAdrese;
                System.out.println("usao sam u equals");
                try {
                    is = s.getInputStream();
                    os = s.getOutputStream();
                    os.write("ERROR11; Adresa vec postoji".getBytes());
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            } else {
                EntitetAdrese novi = new EntitetAdrese(adresa);
                novi.brojZahtjeva = 1;
                Evidencija.zadnjaAdresa = novi;
                Evidencija.sveAdrese.add(novi);
                try {
                    is = s.getInputStream();
                    os = s.getOutputStream();
                    os.write("OK".getBytes());
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    public String userAdresaTest(String adresa) {
        try {
            for (EntitetAdrese entitetAdrese : Evidencija.sveAdrese) {
                if (entitetAdrese.adresa.equals(adresa)) {

                    is = s.getInputStream();
                    os = s.getOutputStream();
                    if (entitetAdrese.status == true) {
                        os.write("OK; YES;".getBytes());
                    } else {
                        os.write("OK; NO;".getBytes());
                    }
                    os.flush();
                    return "";
                } else {
                    try {
                        is = s.getInputStream();
                        os = s.getOutputStream();
                        os.write("ERROR 12; Adresa ne postoji".getBytes());
                        os.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void ServerPause() {
        try {
            if (ServerSustava.serverPauziran == true) {
                
                os.write("ERROR 01; Server je pauziran!".getBytes());
                os.flush();
                return;
            } else {
                ServerSustava.serverPauziran = true;
                os.write("OK;".getBytes());
                os.flush();
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean userWait(String nnnn) {
        this.cekanje = true;
        int pretvorbaInteger = Integer.parseInt(nnnn);
        long milisekunde = Long.valueOf(nnnn) * 1000;
        try {
            sleep(milisekunde);
        } catch (InterruptedException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cekanje = false;
        try {
            is = s.getInputStream();
            os = s.getOutputStream();
            if (this.ispis == true) {
                os.write("OK".getBytes());
                os.flush();
            }

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public synchronized void start() {
        Evidencija.ukupnoZahtjeva++;
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    private void adminStart() {
          try {
            if (ServerSustava.serverPauziran == true) {
                ServerSustava.serverPauziran = false;
                os.write("Ok!".getBytes());
                os.flush();
                return;
            } else { 
                os.write("ERROR 12; Server radi vec!".getBytes());
                os.flush();
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
