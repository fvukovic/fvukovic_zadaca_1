/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.matnovak.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.matnovak.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author grupa_3
 */
public class KorisnikSustava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //-admin -server [ipadresa | adresa] -port port -u korisnik -p lozinka [-pause | -start | -stop | -stat ]
        //TODO dovrši ostale paremetre
        //-admin -server localhost -port 8000
        String sintaksa = "^-admin -server ([^\\s]+) -port ([\\d]{4})$";
        String sintaksaAdmin = "^-admin -server ([^\\s]+) -port ([\\d]{4}) 1";
        String sintaksaPogled = "^-admin -server ([^\\s]+) -port ([\\d]{4}) 2";
        String sintaksaKorisnik = "^-admin -server ([^\\s]+) -port ([\\d]{4}) 2";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            System.out.println("argumenti :" + args[i]);
            sb.append(args[i]).append(" ");
        }
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));

            }

            String nazivServera = m.group(1);
            int port = Integer.parseInt(m.group(2));
            System.out.println("ne valja");
            KorisnikSustava korisnikSustava = new KorisnikSustava();
            korisnikSustava.pokreniKorisnika(nazivServera, port);

        } else {
            p = sb.toString().trim();
            pattern = Pattern.compile(sintaksaAdmin);
            m = pattern.matcher(p);
            status = m.matches();
            if (status) {
                int poc = 0;
                int kraj = m.groupCount();
                for (int i = poc; i <= kraj; i++) {
                    System.out.println(i + ". " + m.group(i));

                }

                String nazivServera = m.group(1);
                int port = Integer.parseInt(m.group(2));
                String nazivFunkcije = "-start";
                System.out.println("valja");
                AdministratorSustava administratorSustava = new AdministratorSustava();
                administratorSustava.pokreniAdministratora(nazivServera, port, nazivFunkcije);

            } else {

                p = sb.toString().trim();
                pattern = Pattern.compile(sintaksaKorisnik);
                m = pattern.matcher(p);
                status = m.matches();
                System.out.println("OVO JE STATUS" + status);
                if (status) {
                    int poc = 0;
                    int kraj = m.groupCount();
                    for (int i = poc; i <= kraj; i++) {
                        System.out.println(i + ". " + m.group(i));

                    }

                    String nazivServera = m.group(1);
                    int port = Integer.parseInt(m.group(2));
                    String nazivFunkcije = "-a";
                    String zadnjiParametar = "400";
                    System.out.println("valja");
                    KlijentSustava klijentSustava = new KlijentSustava();
                    klijentSustava.pokreniKlijenta(nazivServera, port, nazivFunkcije,zadnjiParametar);

                } else {
                    System.out.println("Ne odgovara regex!");
                }
            }
        }
    }

    private void pokreniKorisnika(String nazivServera, int port) {
        InputStream is = null;
        OutputStream os = null;
        Socket s = null;

        try {
            s = new Socket(nazivServera, port);
            is = s.getInputStream();
            os = s.getOutputStream();

            String zahtjev = "USER pero; PASSWD 123456; PAUSE; ";
            os.write(zahtjev.getBytes());
            os.flush();
            s.shutdownOutput();

            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            System.out.println("Primljeni  odgovor: " + sb);
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
    }

}
