package com.deakin.ghosttransmission.Model;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SMS {

    /**
     * Instance Variables
     */
    private String phoneno = null;
    private LocalDateTime timestamp = null;
    private String body = null;
    private String read;

    /**
     * Default Constructor
     */
    public SMS() {
    }

    /**
     * Overloaded Constructor mapping all Instance Variables
     *
     * @param phoneno   address of the sms
     * @param timestamp time and date of the sms
     * @param body      content (message) of the sms
     * @param read      whether the sms has been read
     */
    public SMS(String phoneno, long timestamp, String body, String read) {
        setPhoneno(phoneno);
        setTimestamp(timestamp);
        setBody(body);
        setRead(read); // based on the supplied value, if 1 (True) then the message is 'Read'
    }

    /**
     * Returns a String of data representing the SMS
     *
     * @return Body of the SMS
     */
    @NonNull
    @Override
    public String toString() {
        return getBody();
    }

    /**
     * Getters and Setters
     */
    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()); // convert the given epoch timestamp (ms) to a local date time, as per the system standard
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read.equals("1") ? "Read" : "";
    }
}
