/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
public class RezervnaDretva extends Thread {

    Konfiguracija konf;
    Socket socket;

    public RezervnaDretva(Konfiguracija konf) {
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
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (socket != null) {
                System.out.println("socket moj: " + socket);
                try {
                    InputStream is = null;
                    OutputStream os = null;
                    is = socket.getInputStream();
                    os = socket.getOutputStream(); 
                    os.write("Error 90; Radne dretve su nedostupne!Zao nam je;".getBytes());
                    os.flush();
                    socket.close();
                    socket = null;
                    //TODO provjeri ispravnost pripremljenog zahtjeva

                } catch (IOException ex) {
                    Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //TODO razmisliti kako izaći iz beskonačne petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
