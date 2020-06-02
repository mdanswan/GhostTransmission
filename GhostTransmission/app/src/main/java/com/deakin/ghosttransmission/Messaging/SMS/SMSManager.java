package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;

import java.util.ArrayList;

public class SMSManager {

    /**
     * Instance Variables
     */
    private ArrayList<SMS> smsList = null;
    private ContentResolver contentResolver = null;

    private ArrayList<Conversation> conversations = null;

    /**
     * Constructor
     *
     * @param conversations List of Conversations
     */
    public SMSManager (ArrayList<Conversation> conversations)
    {
        setConversations(conversations);
    }

    private void retrieveConversations(SMSURI from, SMSURI to)
    {
        SMSReader smsReader = new SMSReader(getContentResolver());
        ArrayList<Conversation> conversations = smsReader.readSMS(from, to);
        setConversations(conversations);
    }

    private boolean sendSMS(SMS sms)
    {
        SMSWriter smsWriter = new SMSWriter();
        boolean result = smsWriter.writeSMS(sms);
        return  result;
    }

    /**
     * Getters and Setters
     */
    public ArrayList<SMS> getSmsList() {
        return smsList;
    }

    public void setSmsList(ArrayList<SMS> smsList) {
        this.smsList = smsList;
    }

    public ContentResolver getContentResolver() {
        return contentResolver;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }
}
