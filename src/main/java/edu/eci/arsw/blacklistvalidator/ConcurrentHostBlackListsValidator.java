package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class ConcurrentHostBlackListsValidator extends Thread{

    private HostBlacklistsDataSourceFacade skds;
    private String ipaddress;
    private int lowerBound;
    private int upperBound;

    private static int checkedListsCount = 0;
    private static int ocurrencesCount = 0;
    private static LinkedList<Integer> blackListOcurrences;

    private static final int BLACK_LIST_ALARM_COUNT=5;

    public ConcurrentHostBlackListsValidator(int lowerBound, int upperBound, String ipaddress, HostBlacklistsDataSourceFacade skds){
        this.skds = skds;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.ipaddress = ipaddress;

        blackListOcurrences = new LinkedList<>();
    }

    @Override
    public void run() {
        for(int i = lowerBound; i < upperBound; i++){ 
            increaseCheckedListCounter();
            if(ocurrencesCount < BLACK_LIST_ALARM_COUNT){
                if(skds.isInBlackListServer(i, ipaddress)) addOcurrence(i);
            }else break;
        }
    }

    static public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }

    static public int getCheckedListsCount() {
        return checkedListsCount;
    }

    static public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    synchronized void increaseCheckedListCounter(){
        checkedListsCount++;
    }
    
    synchronized void addOcurrence(int occurrence){
        blackListOcurrences.add(occurrence);
        ocurrencesCount++;
    }
}
