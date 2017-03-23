/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author filip
 */
public class PregledSustava { 
    Konfiguracija konf;

    public PregledSustava() {
        
    }
    
    
        public void prikazi (){
                
        try {
            File f = new File("DZ_1_evidencija.bin"); 
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Evidencija save = (Evidencija)ois.readObject(); 
             System.out.println("Ispis svih adresa:");
        for(EntitetAdrese adresa : save.sveAdrese){
            sleep(500);
            System.err.println("Adresa: "+adresa.adresa+ "; Status: "+adresa.status+"; Broj zahtjeva: "+adresa.brojZahtjeva);
            sleep(500);
        }
            System.err.println("Ukupan broj zahtjeva iznosi: "+save.ukupnoZahtjeva);   
            sleep(1000);
             System.err.println("Ukupan broj uspjesnih zahtjeva iznosi: "+save.brojUspjesnihZahtjeva);
              sleep(1000);
             System.err.println("Ukupan broj uspjesnih zahtjeva iznosi: "+save.brojPrekinutihZahtjeva);
             System.err.println("Ukupno trajanje radnih dretva u sekundama: "+(save.ukupnoTrajanjeRadnihDretva/1000));
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PregledSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PregledSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PregledSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PregledSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
        }
    
    
    
    
            }
