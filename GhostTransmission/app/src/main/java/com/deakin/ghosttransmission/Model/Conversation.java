package com.deakin.ghosttransmission.Model;

import com.deakin.ghosttransmission.Controller.ConversationController;
import com.deakin.ghosttransmission.Database.SQLiteDatabaseManager;
import com.deakin.ghosttransmission.Model.SMS;

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

    public Conversation() {
        fromSmsList = new ArrayList<>();
        toSmsList = new ArrayList<>();
    }

    public Conversation(ArrayList<SMS> fromSmsList, ArrayList<SMS> toSmsList) {
        setFromSmsList(fromSmsList);
        setToSmsList(toSmsList);
        setLastUpdated(LocalDateTime.now());
    }

    public void addFromSMS(SMS newFromSMS) {
        addSMS(newFromSMS, getFromSmsList());
    }

    public void addToSMS(SMS newToSMS) {
        addSMS(newToSMS, getToSmsList());
    }

    public void clearConversation() {
        getFromSmsList().clear();
        getToSmsList().clear();
        setLastUpdated(LocalDateTime.now());
    }

    private void addSMS(SMS newSMS, ArrayList<SMS> listToAddTo) {
        if (newSMS == null)
            return;
        int insertAtIdx = -1;
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
}
