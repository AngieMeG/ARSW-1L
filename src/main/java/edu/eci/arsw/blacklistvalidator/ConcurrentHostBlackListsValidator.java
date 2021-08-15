package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHostBlackListsValidator extends Thread{

    private HostBlacklistsDataSourceFacade skds;
    private String ipaddress;
    private int lowerBound;
    private int upperBound;

    private static AtomicInteger checkedListsCount = new AtomicInteger(0);
    private static AtomicInteger ocurrencesCount = new AtomicInteger(0);
    private static List<Integer> blackListOcurrences = Collections.synchronizedList(new LinkedList<Integer>());

    /**
     * Concurrent validator constructor
     * Validations are performed with the server index, lowerBound and upperBound must be integers in the range 0 - 79999 inclusive
     * @param lowerBoundow Lower server index to be checked
     * @param upperBound Higher server index to be checked
     * @param ipaddress Ip address's string to be consulted
     * @param skds Datasource
     */
    public ConcurrentHostBlackListsValidator(int lowerBound, int upperBound, String ipaddress, HostBlacklistsDataSourceFacade skds){
        this.skds = skds;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.ipaddress = ipaddress;
    }

    @Override
    public void run() {
        for(int i = lowerBound; i < upperBound; i++){ 
            if(ocurrencesCount.get() < HostBlackListsValidator.BLACK_LIST_ALARM_COUNT){
                if(skds.isInBlackListServer(i, ipaddress)) addOcurrence(i);
            }else break;
            increaseCheckedListCounter();
        }
    }

    static public LinkedList<Integer> getBlackListOcurrences() {
        return new LinkedList<Integer>(blackListOcurrences);
    }

    static public int getCheckedListsCount() {
        return checkedListsCount.get();
    }

    static public int getOcurrencesCount() {
        return ocurrencesCount.get();
    }

    /**
     * Thread safe function to increase the checked list counter
     */
    synchronized void increaseCheckedListCounter(){
        checkedListsCount.incrementAndGet();
    }
    
    /**
     * Thread safe function to add new occurences to the black list ocurrence LinkedList
     * Ocurrences counter automatically increase when a new ocurrence is added.
     * @param occurrence The new ocurrence to be added
     */
    synchronized void addOcurrence(int occurrence){
        blackListOcurrences.add(occurrence);
        ocurrencesCount.incrementAndGet();
    }
}
