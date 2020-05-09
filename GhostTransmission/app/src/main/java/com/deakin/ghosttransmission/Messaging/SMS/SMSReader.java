package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.deakin.ghosttransmission.Model.SMS;

import java.util.ArrayList;

public class SMSReader {

    /**
     * Constants
     */
    private final String URI = "content://sms/inbox";

    /**
     * Instance Variables
     */
    private ContentResolver contentResolver = null;

    /**
     * Default Constructor
     *
     * @param contentResolver Content Resolver
     */
    public SMSReader(ContentResolver contentResolver) {
        // init content resolver object
        setContentResolver(contentResolver);
    }

    public ArrayList<SMS> ReadSMS() {
        Cursor c = getContentResolver().query(Uri.parse(URI), null, null, null, null);
        ArrayList<SMS> smsArrayList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                SMS s = new SMS();
                for (int i = 0; i < c.getColumnCount(); i++)
                    s.setContent(s.getContent() + c.getColumnName(i) + ": " + c.getString(i) + "\n");
                smsArrayList.add(s);
            } while (c.moveToNext());
        } else {
            Log.d("SMS Reader", "No messages available");
        }
        if (!c.isClosed())
            c.close();
        return smsArrayList;
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
