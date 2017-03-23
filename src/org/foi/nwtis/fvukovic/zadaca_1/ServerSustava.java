/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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
public class ServerSustava {

    /**
     * @param args the command line arguments
     */
   
    public static List<RadnaDretva> listaRadnihDretva = new ArrayList<RadnaDretva>();
    public static boolean serverPauziran=false; 
    

    public static void main(String[] args) {
        //-konf datoteka(.txt | .xml) [-load]

        String sintaksa = "^-konf ([^\\s]+\\.(?i))(txt|xml|bin)( +-load)?$";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
            System.out.println("argumenti :" + args[i]);

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

            String nazivDatoteke = m.group(1) + m.group(2);
            boolean trebaUcitatiEvidenciju = false;
            if (m.group(3) != null) {
                trebaUcitatiEvidenciju = true;
            }

            ServerSustava server = new ServerSustava();
            server.pokreniServer(nazivDatoteke, trebaUcitatiEvidenciju);

        } else {
            System.out.println("Ne odgovara!");
        }
    }

    public static void ugasiAplikaciju() {
        System.exit(0);
    }

    public static void serverStop() {

    }

    private void pokreniServer(String nazivDatoteke, boolean trebaUcitatiEvidenciju) {
        //TODO kreirati kolekciju u kojoj će se spremati aktivne dretve
        try {
            Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
            Short redniBrojDretve = 1;
            int port = Integer.parseInt(konfig.dajPostavku("port"));
            int brojMaximalnihRadnihDretva = Integer.parseInt(konfig.dajPostavku("maksBrojRadnihDretvi"));

            NadzorDretvi nd = new NadzorDretvi(konfig);
            nd.start();
            RezervnaDretva rezervnaDretva = new RezervnaDretva(konfig);
            rezervnaDretva.start();
            ProvjeraAdresa pa = new ProvjeraAdresa(konfig);
            pa.start();
            SerijalizatorEvidencije se = new SerijalizatorEvidencije(konfig);
            se.start();

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                 
           
                    
                Socket socket = serverSocket.accept();

                if (trebaUcitatiEvidenciju == true) {
                    File f = new File(konfig.dajPostavku("evidDatoteka"));
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    try {
                        Evidencija save = (Evidencija) ois.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                }else{
                    System.out.println("Ne postoji datoteka sa serijaliziranim podacima");
                }

                int parsiranRedniBroj = Integer.parseInt(redniBrojDretve.toString());

                if (listaRadnihDretva.size() < brojMaximalnihRadnihDretva) {
                    RadnaDretva rd = new RadnaDretva(socket, konfig);
                    rd.setName("fvukovic - " + parsiranRedniBroj);
                    parsiranRedniBroj++;
                    redniBrojDretve = (short) parsiranRedniBroj;
                    System.out.println(rd.getName());
                    System.out.println(listaRadnihDretva.size());
                    listaRadnihDretva.add(rd);
                    rd.start();
                    rd.listaSvihRadnihDretva = listaRadnihDretva;
                } else {
                    rezervnaDretva.socket = socket;
                }

                //TODO treba provjeriti ima li "mjesta" za novu radnu dretvu
             
        }

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija | IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
