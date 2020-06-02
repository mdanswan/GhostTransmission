package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SMSReader {

    /**
     * Constants
     */
    private final String[] SMS_COLS = new String[]{"address", "date", "body", "read"};
    private final String SMS_SORT_ORDER = "date DESC LIMIT 100";

    /**
     * Instance Variables
     */
    private ContentResolver contentResolver = null;

    /**
     * Constructor
     *
     * @param contentResolver Content Resolver
     */
    public SMSReader(ContentResolver contentResolver) {
        // init content resolver object
        setContentResolver(contentResolver);
    }

    public ArrayList<Conversation> readSMS(SMSURI from, SMSURI to) {

        Map<String, ArrayList<SMS>> fromSmsListAsConversation = readSmsAsConversation(from);
        Map<String, ArrayList<SMS>> toSmsListAsConversation = readSmsAsConversation(to);

        return mergeConversations(fromSmsListAsConversation, toSmsListAsConversation);
    }

    private Map<String, ArrayList<SMS>> readSmsAsConversation(SMSURI smsuri) {
        Map<String, ArrayList<SMS>> conversationFromList = new HashMap<>();

        Cursor c = getContentResolver().query(Uri.parse(
                smsuri.getUri()),
                SMS_COLS,
                "",
                null,
                SMS_SORT_ORDER);

        if (!c.moveToFirst())
            return conversationFromList;

        do {
            final SMS s = new SMS();
            s.setPhoneno(c.getString(c.getColumnIndex("address")));
            s.setTimestamp(c.getLong(c.getColumnIndex("date")));
            s.setBody(c.getString(c.getColumnIndex("body")));
            s.setRead(c.getString(c.getColumnIndex("read")));

            // check if the new SMS is part of one of the already constructed Conversations
            if (conversationFromList.containsKey(s.getPhoneno())) {
                ArrayList<SMS> smsList = conversationFromList.get(s.getPhoneno());
                smsList.add(smsList.size(), s);
            } else // if the Conversations is not present, create a new one and append the new SMS instance
                conversationFromList.put(s.getPhoneno(), new ArrayList<SMS>(Collections.singletonList(s)));
        } while (c.moveToNext());

        c.close();

        return conversationFromList;
    }

    private ArrayList<Conversation> mergeConversations(Map<String, ArrayList<SMS>> fromSmsList, Map<String, ArrayList<SMS>> toSmsList) {

        ArrayList<Conversation> conversations = new ArrayList<>();

        Object[] fromKeys = fromSmsList.keySet().toArray();
        Object[] toKeys = toSmsList.keySet().toArray();

        int i = 0;
        while (fromSmsList.size() != 0) {
            Conversation c = new Conversation();
            c.setFromSmsList(fromSmsList.get(fromKeys[i]));
            fromSmsList.remove(fromKeys[i]);
            if (toSmsList.containsKey(fromKeys[i])) {
                c.setToSmsList(toSmsList.get(fromKeys[i]));
                toSmsList.remove(fromKeys[i]);
            }
            conversations.add(c);
            i++;
        }

        for (String sms : toSmsList.keySet()) {
            Conversation c = new Conversation();
            c.setToSmsList(toSmsList.get(sms));
            conversations.add(c);
        }

        return conversations;
    }

    /**
     * Utility method to find the max rows of a Cursor, from a given Cursor Array
     *
     * @param cursors Cursor Array
     * @return Max Rows in a Cursor
     */
    private int getMaxCursorRows(Cursor[] cursors) {
        int maxRows = -1;
        for (int i = 0; i < cursors.length; i++)
            if (cursors[i].getCount() > maxRows)
                maxRows = cursors[i].getCount();
        return maxRows;
    }

    /**
     * Getters and Setters
     */
    private ContentResolver getContentResolver() {
        return contentResolver;
    }

    private void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
}
