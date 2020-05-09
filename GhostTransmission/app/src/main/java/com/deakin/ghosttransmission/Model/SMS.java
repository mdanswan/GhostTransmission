package com.deakin.ghosttransmission.Model;

import androidx.annotation.NonNull;

public class SMS {

    /**
     * Instance Variables
     */
    private String content = null;
    private String from = null;

    /**
     * Default Constructor
     */
    public SMS() {
    }

    /**
     * Overloaded Constructor mapping all Instance Variables
     * @param sms message text
     */
    public SMS(String sms) {
        setMessage(sms);
    }

    /**
     * Returns a String of data representing the SMS
     * @return Data and Contents of the SMS
     */
    @NonNull
    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * Getters and Setters
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
