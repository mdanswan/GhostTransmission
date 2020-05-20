package com.deakin.ghosttransmission.Model;

public enum SMSURI {
    INBOX_URI("content://sms/inbox"),
    SENT_URI("content://sms/sent");

    private final String uri;

    SMSURI(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
