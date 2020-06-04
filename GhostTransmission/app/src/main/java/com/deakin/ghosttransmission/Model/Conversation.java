package com.deakin.ghosttransmission.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Conversation {

    /**
     * Instance Variables
     */
    private String fromAddress = null;
    private String identity = null;
    private LocalDateTime lastUpdated = null;

    private ArrayList<SMS> fromSmsList = null;
    private ArrayList<SMS> toSmsList = null;
    private ArrayList<SMS> mergedList = null;

    /**
     * Constructor
     */
    public Conversation() {
        fromSmsList = new ArrayList<>();
        toSmsList = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param fromSmsList from sms list
     * @param toSmsList   to sms list
     */
    public Conversation(ArrayList<SMS> fromSmsList, ArrayList<SMS> toSmsList) {
        setFromSmsList(fromSmsList);
        setToSmsList(toSmsList);
        setLastUpdated(LocalDateTime.now());
    }

    /**
     * Add a new SMS to the from sms list
     *
     * @param newFromSMS new from sms
     */
    public void addFromSMS(SMS newFromSMS) {
        addSMS(newFromSMS, getFromSmsList());
    }

    /**
     * Add a new SMS to the to sms list
     *
     * @param newToSMS new to sms
     */
    public void addToSMS(SMS newToSMS) {
        addSMS(newToSMS, getToSmsList());
    }

    /**
     * Clear the data associated with this Conversation
     */
    public void clearConversation() {
        getFromSmsList().clear();
        getToSmsList().clear();
        setLastUpdated(LocalDateTime.now());
    }

    /**
     * Add the given SMS instance to the given list (in order of date descending)
     *
     * @param newSMS      new sms instance
     * @param listToAddTo list to add the sms instance to
     */
    private void addSMS(SMS newSMS, ArrayList<SMS> listToAddTo) {
        // check if the new sms is null
        if (newSMS == null)
            return;

        int insertAtIdx = 0;
        // decide where to insert the new sms instance based on date
        for (SMS sms : getFromSmsList()) {
            if (newSMS.getTimestamp().isBefore(sms.getTimestamp())) {
                insertAtIdx = listToAddTo.indexOf(sms);
                break;
            }
        }

        listToAddTo.add(insertAtIdx, newSMS);
        setLastUpdated(LocalDateTime.now());
    }

    /**
     * Merges the two internal (from and to) lists together, ordering based on Date descending
     */
    public void updateMergedList() {
        ArrayList<SMS> mergedSMSList = new ArrayList<>();

        for (int i = 0, e = 0; i < getFromSmsList().size() || e < getToSmsList().size(); ) {
            // check whether from or to in the most recent SMS
            SMS from = null, to = null;

            // check if the from an to SMS lists contain the next index (i/e)
            if (getFromSmsList().size() > i)
                from = getFromSmsList().get(i);

            if (getToSmsList().size() > e)
                to = getToSmsList().get(e);

            // if we can compare from and to timestamps
            if (from != null && to != null) {
                // add the most recent sms to the merged sms list
                if (from.getTimestamp().isBefore(to.getTimestamp())) {
                    mergedSMSList.add(to);
                    e++;
                } else {
                    mergedSMSList.add(from);
                    i++;
                }
            } else if (from == null) { // if from sms is unavailable then add to sms
                mergedSMSList.add(to);
                e++;
            } else if (to == null) {  // if to sms is unavailable then add from sms
                mergedSMSList.add(from);
                i++;
            }
        }

        setMergedList(mergedSMSList);
    }

    public int getConversationSize() {
        return getFromSmsList().size() + getToSmsList().size();
    }

    /**
     * Getters and Setters
     */
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<SMS> getFromSmsList() {
        return fromSmsList;
    }

    public void setFromSmsList(ArrayList<SMS> fromSmsList) {
        this.fromSmsList = fromSmsList;
    }

    public ArrayList<SMS> getToSmsList() {
        return toSmsList;
    }

    public void setToSmsList(ArrayList<SMS> toSmsList) {
        this.toSmsList = toSmsList;
    }

    public ArrayList<SMS> getMergedList() {

        // check if the merged list is null, if so, create the merged list
        if (mergedList == null)
            updateMergedList();

        return mergedList;
    }

    public void setMergedList(ArrayList<SMS> mergedList) {
        this.mergedList = mergedList;
    }
}
