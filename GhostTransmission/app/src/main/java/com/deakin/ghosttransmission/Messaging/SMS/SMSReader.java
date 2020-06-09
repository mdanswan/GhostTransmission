package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SMSReader {

    /**
     * Constants
     */
    private final String[] SMS_COLS = new String[]{"address", "date", "body", "read"};
    private final String[] SMS_COLS_DISTINCT = new String[]{"DISTINCT address, date"};
    private final int DEFAULT_LIM = 100;
    private final String SMS_SORT_ORDER = "date DESC" + " LIMIT " + DEFAULT_LIM;
    private final int MAX_LIM = Integer.MAX_VALUE;

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

    /**
     * Reads both sides of a Conversation (from and to)
     *
     * @param from from SMSURI
     * @param to   to SMSURI
     * @return list of Conversations
     */
    public ConversationList readSMS(SMSURI from, SMSURI to) {

        Map<String, ArrayList<SMS>> fromSmsListAsConversation = readSMSAsConversation(from);
        Map<String, ArrayList<SMS>> toSmsListAsConversation = readSMSAsConversation(to);

        return mergeConversations(fromSmsListAsConversation, toSmsListAsConversation);
    }

    /**
     * Reads both sides of a specific Conversation
     *
     * @param from    from SMSURI
     * @param to      to SMSURI
     * @param address address of from participant
     * @param limit   limit the number of messages returned (<1 is interpreted as all messages)
     * @return conversation between from and to participants
     */
    public Conversation readSMS(SMSURI from, SMSURI to, String address, int limit) {
        Conversation c = new Conversation();
        c.setFromSmsList(readSMSListWithParams(from, address, limit));
        c.setToSmsList(readSMSListWithParams(to, address, limit));
        c.setFromAddress(address);
        return c;
    }

    public HashMap<Integer, String> readDistinctSMSAddresses(SMSURI from, SMSURI to) {
        HashMap<Integer, String> distinctSMSAddresses = new HashMap<>(); // using HashSet for efficiency and distinct values
        HashSet<String> addressHistory = new HashSet<>(); // to keep track of which address has been added as distinct

        Cursor c1 = getContentResolver().query(Uri.parse(
                from.getUri()),
                SMS_COLS_DISTINCT,
                "",
                null,
                "date DESC");

        Cursor c2 = getContentResolver().query(Uri.parse(
                to.getUri()),
                SMS_COLS_DISTINCT,
                "",
                null,
                "date DESC");

        // check if we can move to the first place in the c1 and c2 Cursors
        boolean isC1Empty = !c1.moveToFirst();
        boolean isC2Empty = !c2.moveToFirst();

        if (isC1Empty && isC2Empty)
            return distinctSMSAddresses;

        boolean isC1Distinct = false, isC2Distinct = false, isC1MoreThanC2 = false;

        // add unique items to distinct addresses based on date (with index as key, and address as value)
        int index = 0;
        do {
            String c1Address = c1.getString(c1.getColumnIndex("address"));
            String c2Address = c2.getString(c2.getColumnIndex("address"));

            // check if the current addresses are already in the distinct set
            isC1Distinct = !addressHistory.contains(c1Address);
            isC2Distinct = !addressHistory.contains(c2Address);

            // if both current addresses are not distinct, begin the loop again
            if (!isC1Distinct && !isC2Distinct) {
                c1.moveToNext();
                c2.moveToNext();
                continue;
            }

            // get the data of the current addresses
            long c1Date = c1.getLong(c1.getColumnIndex("date"));
            long c2Date = c2.getLong(c2.getColumnIndex("date"));

            // compare c1 date to c2 date
            isC1MoreThanC2 = (c1Date > c2Date);

            if (isC1Distinct && isC1MoreThanC2) {
                distinctSMSAddresses.put(index++, c1Address);
                addressHistory.add(c1Address);
                c1.moveToNext();
            } else if (!isC1Distinct) {
                c1.moveToNext();
            }

            if (isC2Distinct && !isC1MoreThanC2) {
                distinctSMSAddresses.put(index++, c2Address);
                addressHistory.add(c2Address);
                c2.moveToNext();
            } else if (!isC2Distinct) {
                c2.moveToNext();
            }
        }
        while (!c1.isAfterLast() && !c2.isAfterLast());

        c1.close();
        c2.close();

        return distinctSMSAddresses;
    }

    /**
     * Reads one side of a Conversation (from or to) in the form of a Conversation
     *
     * @param smsuri from or to SMSURI
     * @return map of address, messages
     */
    private Map<String, ArrayList<SMS>> readSMSAsConversation(SMSURI smsuri) {
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
                conversationFromList.put(s.getPhoneno(), new ArrayList<>(Collections.singletonList(s)));
        } while (c.moveToNext());

        c.close();

        return conversationFromList;
    }

    /**
     * Reads one side (from or to) of a Conversation
     *
     * @param smsuri  from or To SMSURI
     * @param address address of the From participant
     * @param limit   limit of the number of rows to retrieve
     * @return a list of SMS instances
     */
    private ArrayList<SMS> readSMSListWithParams(SMSURI smsuri, String address, int limit) {
        ArrayList<SMS> smsList = new ArrayList<>();

        // check if the given value is zero or negative, if so, replace with integer maximum
        if (limit < 1)
            limit = MAX_LIM;

        Cursor c = getContentResolver().query(Uri.parse(
                smsuri.getUri()),
                SMS_COLS,
                "address = ?",
                new String[]{address},
                "date DESC LIMIT " + limit);

        if (!c.moveToFirst())
            return smsList;

        do {
            final SMS s = new SMS();
            s.setPhoneno(c.getString(c.getColumnIndex("address")));
            s.setTimestamp(c.getLong(c.getColumnIndex("date")));
            s.setBody(c.getString(c.getColumnIndex("body")));
            s.setRead(c.getString(c.getColumnIndex("read")));
            smsList.add(s);
        } while (c.moveToNext());

        c.close();

        return smsList;
    }

    /**
     * Merges the two given sides of a Conversation (from and to)
     *
     * @param fromSmsList from SMS list
     * @param toSmsList   to SMS list
     * @return list of Conversations
     */
    private ConversationList mergeConversations
    (Map<String, ArrayList<SMS>> fromSmsList, Map<String, ArrayList<SMS>> toSmsList) {

        ConversationList conversations = new ConversationList();

        Object[] fromKeys = fromSmsList.keySet().toArray();
        Object[] toKeys = toSmsList.keySet().toArray();

        int i = 0;
        while (fromSmsList.size() != 0) {
            Conversation c = new Conversation();
            c.setFromSmsList(fromSmsList.get(fromKeys[i]));
            String address = (String) fromKeys[i];
            c.setFromAddress(address);
            c.setIdentity(address);
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
            c.setFromAddress(sms);
            c.setIdentity(sms);
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
