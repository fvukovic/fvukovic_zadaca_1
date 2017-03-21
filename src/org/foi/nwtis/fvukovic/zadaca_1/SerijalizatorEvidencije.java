/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fvukovic.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.matnovak.konfiguracije.Konfiguracija;

/**
 *
 * @author grupa_3
 */
public class SerijalizatorEvidencije extends Thread{

    Konfiguracija konf;
    
    public SerijalizatorEvidencije(Konfiguracija konf) {
        this.konf = konf;
    }
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
       
            //TODO dovršite sami
       
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        Evidencija objektZaSerijalizaciju = new Evidencija();
        File f = new File(konf.dajPostavku("evidDatoteka"));
         System.out.println("Usao sam u serijalizaciju");
        try {
            
         System.out.println("Usao sam u serijalizaciju broj 2");
            FileOutputStream fos = new FileOutputStream(f);            
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(objektZaSerijalizaciju);
            Evidencija pregled = getEvidenciju();
            System.out.println( "ZAHTJEVI: "+pregled.ukupnoZahtjeva);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public Evidencija getEvidenciju() throws FileNotFoundException, IOException, ClassNotFoundException{
        File f = new File(konf.dajPostavku("evidDatoteka"));
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Evidencija save = (Evidencija)ois.readObject();         
        return save;
         
    }
    
}
