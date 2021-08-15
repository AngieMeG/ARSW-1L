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

    public ConcurrentHostBlackListsValidator(int lowerBound, int upperBound, String ipaddress, HostBlacklistsDataSourceFacade skds){
        this.skds = skds;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.ipaddress = ipaddress;
    }

    public ConcurrentHostBlackListsValidator(){};

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

    synchronized void increaseCheckedListCounter(){
        checkedListsCount.incrementAndGet();
    }
    
    synchronized void addOcurrence(int occurrence){
        blackListOcurrences.add(occurrence);
        ocurrencesCount.incrementAndGet();
    }
}
