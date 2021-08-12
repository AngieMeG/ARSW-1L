/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

import java.util.ArrayList;

/**
 * Implementation to handle the threads of the class CountThread
 * @author hcadavid
 * @author Angie Medina
 * @author Jose Perex
 */
public class CountThreadsMain {
    public static void main(String a[]){
        ArrayList<CountThread> threads = new ArrayList<>();
        threads.add(new CountThread(0,99));
        threads.add(new CountThread(99, 199));
        threads.add(new CountThread(200,299));
        for(CountThread thread : threads) thread.run();
    }
    
}
