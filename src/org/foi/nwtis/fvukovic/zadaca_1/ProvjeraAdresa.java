/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
public class ProvjeraAdresa extends Thread {

    Konfiguracija konf;

    public ProvjeraAdresa(Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku("intervalAdresneDretve"));

        while (true) {
            System.out.println(this.getClass());
            long trenutnoVrijeme = System.currentTimeMillis();
            //TODO dovršite sami
            long vrijemeZavrsetka = System.currentTimeMillis();

            try {
                sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (EntitetAdrese entitetAdrese : Evidencija.sveAdrese) {
                  
                try {
                    boolean reachable = InetAddress.getByName(entitetAdrese.adresa).isReachable(10000);
                   
                    if (reachable == true) {
                        entitetAdrese.status = true;
                    } else {
                        entitetAdrese.status = false;
                    }

                } catch (UnknownHostException ex) {
                    entitetAdrese.status = false;
                } catch (IOException ex) {
                    Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
                }
                     System.out.println("statusi adrese: "+ entitetAdrese.adresa+entitetAdrese.status);
            }

            //TODO razmisliti kako izaći iz beskonačne petlje
            //TODO razmisliti kako izaći iz beskonačne petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
