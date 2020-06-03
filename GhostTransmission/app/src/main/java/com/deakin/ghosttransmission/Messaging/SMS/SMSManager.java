package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;

import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;

import java.util.ArrayList;

public class SMSManager {

    /**
     * Instance Variables
     */
    private ArrayList<SMS> smsList = null;
    private ContentResolver contentResolver = null;
    private ConversationList conversations = null;

    /**
     * Constructor
     */
    public SMSManager(ContentResolver contentResolver) {
        setContentResolver(contentResolver);
    }

    /**
     * Constructor
     *
     * @param conversations List of Conversations
     */
    public SMSManager(ConversationList conversations) {
        setConversations(conversations);
    }

    /**
     * Retrieves all messages (from and to) within the designated limit (see SMSReader) and merges
     * these messages together into Conversations.
     * <p>
     * The Conversations are stored within this class, for access through Getters and Setters
     *
     * @param from from SMSURI
     * @param to   to SMSURI
     */
    public void retrieveConversations(SMSURI from, SMSURI to) {
        SMSReader smsReader = new SMSReader(getContentResolver());
        ConversationList conversations = smsReader.readSMS(from, to);
        setConversations(conversations);
    }

    public Conversation retrieveConversation(SMSURI from, SMSURI to, String address, int limit) {
        SMSReader smsReader = new SMSReader(getContentResolver());
        Conversation conversation = smsReader.readSMS(from, to, address, limit);
        return conversation;
    }

    /**
     * Creates a list of Conversation addresses from the internal Conversation ArrayList
     *
     * @return List of Conversations Addresses
     */
    public ArrayList<String> getConversationAddressList() {
        ArrayList<String> conversationAddressList = new ArrayList<>();

        for (Conversation c : conversations)
            conversationAddressList.add(c.getFromAddress());

        return conversationAddressList;
    }

    /**
     * Sends the given SMS via the default Subscription service
     *
     * @param sms SMS Model with address, and message
     * @return whether the SMS was successfully sent
     */
    private boolean sendSMS(SMS sms) {
        SMSWriter smsWriter = new SMSWriter();
        boolean result = smsWriter.writeSMS(sms);
        return result;
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

    public ConversationList getConversations() {
        return conversations;
    }

    public void setConversations(ConversationList conversations) {
        this.conversations = conversations;
    }
}
