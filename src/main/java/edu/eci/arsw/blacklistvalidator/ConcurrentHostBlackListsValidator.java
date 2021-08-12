package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class ConcurrentHostBlackListsValidator extends Thread{

    private HostBlacklistsDataSourceFacade skds;
    private int lowerBound;
    private int upperBound;
    private int checkedListsCount;
    private String ipaddress;
    private LinkedList<Integer> blackListOcurrences;

    public ConcurrentHostBlackListsValidator(int lowerBound, int upperBound, String ipaddress, HostBlacklistsDataSourceFacade skds){
        this.skds = skds;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.ipaddress = ipaddress;

        blackListOcurrences = new LinkedList<>();
        checkedListsCount = 0;
    }

    @Override
    public void run() {
        for(int i=lowerBound; i<upperBound; i++){ 
            if(skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                checkedListsCount++;
            }
        }

    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }
    
}
