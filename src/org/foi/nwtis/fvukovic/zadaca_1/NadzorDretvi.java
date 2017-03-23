/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
public class NadzorDretvi extends Thread{

    Konfiguracija konf; 
    
    public NadzorDretvi(Konfiguracija konf) {
        this.konf = konf;
    }
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku("intervalNadzorneDretve"));
        int trajanjeSpavanjaRadneDretve = Integer.parseInt(konf.dajPostavku("maksVrijemeRadneDretve"));
        while (true) {    
            System.out.println(this.getClass());
            long trenutnoVrijeme = System.currentTimeMillis();
            if(ServerSustava.listaRadnihDretva!=null){
                System.out.println("DRETVE: "+ServerSustava.listaRadnihDretva.size());
            for(RadnaDretva radnaDretva : ServerSustava.listaRadnihDretva){ 
                long vrijemeZavrsetka = System.currentTimeMillis();
                int brojac=0;
                long vrijemeIzvodenjaDretve = (vrijemeZavrsetka - radnaDretva.vrijemeIzvršavanja);
                 
                System.out.println(vrijemeIzvodenjaDretve>trajanjeSpavanjaRadneDretve);
                if(vrijemeIzvodenjaDretve>trajanjeSpavanjaRadneDretve){
                    radnaDretva.interrupt();
                    ServerSustava.listaRadnihDretva.remove(0);
                    System.out.println("Iskljucena je radna dretva pod imenom: " + radnaDretva.getName());
                    System.out.println("Broj Radnih Dretva: "+ServerSustava.listaRadnihDretva.size());
                    if(ServerSustava.listaRadnihDretva.size()==0){
                        break;
                    }
                }else{
                    System.err.println("Vrijeme trajanja je dobro ");
                }
                brojac++;
                }
            }
            long vrijemeZavrsetka = System.currentTimeMillis();
            
            try {
                sleep(trajanjeSpavanja- (vrijemeZavrsetka - trenutnoVrijeme));
            } catch (InterruptedException ex) {
                Logger.getLogger(NadzorDretvi.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //TODO razmisliti kako izaći iz beskonačne petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
