package com.deakin.ghosttransmission.Messaging.SMS;

import android.telephony.SmsManager;

import com.deakin.ghosttransmission.Model.SMS;

public class SMSWriter {

    public SMSWriter() {
    }

    public boolean writeSMS(SMS sms) throws IllegalArgumentException {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.getPhoneno(), null, sms.getBody(), null, null);
        return true;
    }
}
