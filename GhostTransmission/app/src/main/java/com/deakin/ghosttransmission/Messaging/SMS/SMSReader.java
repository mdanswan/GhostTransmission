package com.deakin.ghosttransmission.Messaging.SMS;

import android.content.ContentResolver;
import android.database.Cursor;

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
    private Cursor cursor = null;

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

    private Cursor getCursor() {
        return cursor;
    }

    private void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
