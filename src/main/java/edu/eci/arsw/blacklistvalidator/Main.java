/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.lang.Runtime;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        
        Runtime vjm = Runtime.getRuntime();
        int numberCores = vjm.availableProcessors();
        numberCores = 600;
        
        System.out.println("\nNumber of Threads: "+numberCores);
        System.out.println("Performance: " + testByThreadsNumber("212.24.24.55", numberCores));
        System.out.println();
    }

    public static String testByThreadsNumber(String ipaddress, int threadsNumber){

        HostBlackListsValidator hblv=new HostBlackListsValidator();
        
        long t1 = System.currentTimeMillis(), performance;
        List<Integer> blackListOcurrences = hblv.checkHost(ipaddress,threadsNumber);
        performance = System.currentTimeMillis() - t1;
        System.out.println("The host was found in the following blacklists:" + blackListOcurrences);

        return "" + performance + "ms";

    }
    
}
