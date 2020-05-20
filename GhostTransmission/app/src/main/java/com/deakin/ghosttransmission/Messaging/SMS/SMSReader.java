package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;

import java.util.ArrayList;

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

    public ArrayList<SMS> ReadSMS(SMSURI... smsuri) {

        ArrayList<SMS> smsList = new ArrayList<>(); // list to hold the resulting SMS messages
        Cursor[] cursors = new Cursor[smsuri.length];

        // query content providers using the specified URIs
        for (int i = 0; i < cursors.length; i++) {
            Cursor c = getContentResolver().query(Uri.parse(
                    smsuri[i].getUri()),
                    SMS_COLS,
                    null,
                    null,
                    SMS_SORT_ORDER);
            cursors[i] = c;
        }

        // move all cursors to their first element, voiding all 'empty' Cursors
        for (int i = 0; i < cursors.length; i++) {
            if (!cursors[i].moveToFirst())
                cursors[i] = null;
        }

        // traverse all cursors, adding SMS instances in 'descending' order of date_sent
        for (int i = 0; i < getMaxCursorRows(cursors); i++) {

            Cursor cHigh = null;

            for (Cursor c : cursors) {
                if (c == null)
                    continue;

                if (cHigh == null)
                    cHigh = c;
                else if (c.getLong(c.getColumnIndex("date")) > cHigh.getLong(cHigh.getColumnIndex("date"))) {
                    Log.d("SENT", "SENT");
                    cHigh = c;
                }
            }

            // create a new SMS instance with the relevant SMS data
            SMS s = new SMS();
            s.setPhoneno(cHigh.getString(cHigh.getColumnIndex("address")));
            s.setTimestamp(cHigh.getLong(cHigh.getColumnIndex("date")));
            s.setBody(cHigh.getString(cHigh.getColumnIndex("body")));
            s.setRead(cHigh.getString(cHigh.getColumnIndex("read")));

            // add the new SMS instance to the SMS collection
            smsList.add(i, s);

            // move the High cursor to the next position, and void if no remaining items
            if (!cHigh.moveToNext())
                cHigh = null;
        }

        // close all active cursors
        for (Cursor c : cursors)
            if (c != null)
                if (!c.isClosed())
                    c.close();

        return smsList;
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
