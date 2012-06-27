/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serwer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jerzy
 */
public class Serwer {
ArrayList<PrintWriter> ludzie;
    /**
     * @param args the command line arguments
     */
        public class ObslugaKlientow implements Runnable{
            BufferedReader czytelnik;
            Socket gniazdo;
            public ObslugaKlientow(Socket clientSocket){
                try{
                    gniazdo = clientSocket;
                    InputStreamReader rd = new InputStreamReader(gniazdo.getInputStream());
                    czytelnik = new BufferedReader(rd);
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                
            }
            public void run(){
                String wiadomosc;
                try{
                    while((wiadomosc = czytelnik.readLine())!=null){
                        System.out.println("Odczytano: "+wiadomosc);
                        rozeslijDoWszystkich(wiadomosc);
                }
            } catch(Exception ex){
                ex.printStackTrace();
        }
            }
        }
    public static void main(String[] args) {
       new Serwer().doRoboty();

    }
    public void doRoboty(){
        ludzie = new ArrayList<PrintWriter>();
        try{
            ServerSocket serverSock = new ServerSocket(8189);
            while(true){
                Socket gniazdoKlienta = serverSock.accept();
                PrintWriter pisarz = new PrintWriter(gniazdoKlienta.getOutputStream());
                ludzie.add(pisarz);
                Thread t = new Thread(new ObslugaKlientow(gniazdoKlienta));
                t.start();
                System.out.println("No");
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void rozeslijDoWszystkich(String message){
        Iterator it = ludzie.iterator();
        while(it.hasNext()){
            try{
                PrintWriter pisarz = (PrintWriter) it.next();
                pisarz.println(message);
                pisarz.flush();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
