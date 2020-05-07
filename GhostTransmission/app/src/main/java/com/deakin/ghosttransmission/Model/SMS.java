package com.deakin.ghosttransmission.Model;

public class SMS {

    /**
     * Instance Variables
     */
    private String message = null;

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
     * Getters and Setters
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
