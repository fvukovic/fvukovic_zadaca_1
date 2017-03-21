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

/**
 *
 * @author filip
 */
public class KlijentSustava {
    public void pokreniKlijenta(String nazivServera, int port, String nazivFunkcije,String zadnjiParametar) {
        InputStream is = null;
        OutputStream os = null;
        Socket s = null;

        try {
            s = new Socket(nazivServera, port);
            is = s.getInputStream();
            os = s.getOutputStream();
            String zahtjev = "";
            if(nazivFunkcije.equals("-a")){
             zahtjev = "USER pero; ADD "+zadnjiParametar+";";
            }
            if(nazivFunkcije.equals("-t")){
             zahtjev = "USER pero; TEST "+zadnjiParametar+";";
            }
            if(nazivFunkcije.equals("-w")){
             zahtjev = "USER pero; WAIT "+zadnjiParametar+";";
            }
            System.out.println(zahtjev);
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
