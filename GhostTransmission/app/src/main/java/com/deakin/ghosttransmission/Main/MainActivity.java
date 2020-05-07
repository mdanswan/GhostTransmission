package com.deakin.ghosttransmission.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.deakin.ghosttransmission.Messaging.SMS.SMSReader;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * Constants
     */
    private final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(Manifest.permission.READ_SMS, REQUEST_CODE);

        SMSReader smsReader = new SMSReader(getContentResolver());
        ArrayList<SMS> texts = smsReader.ReadSMS();
        for (SMS sms : texts)
            Log.d("SMS TO STRING", sms.toString());
    }

    public boolean requestPermissions(String permission, final int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return true; // assuming granted permission result
        } else {
            return false; // permission not granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
