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
import android.widget.Toast;

import com.deakin.ghosttransmission.Messaging.SMS.SMSReader;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * Instance Variables
     */
    private Map<String, Integer> permissions = null; // permission to request

    /**
     * Constants
     */
    private final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init permissions arrays
        permissions = new HashMap<>();

        // request permissions required for application
        requestPermission(Manifest.permission.READ_SMS, REQUEST_CODE);

        // create adaptor for main screen received sms

        // create data structure as source for adapter

        // set main recycler view adapter as the one above

        SMSReader smsReader = new SMSReader(getContentResolver());
        ArrayList<SMS> texts = smsReader.ReadSMS();
        for (int i = 0; i < 10; i++)
            Log.d("SMS TO STRING", texts.get(i).toString());
    }

    /**
     * Requests a single permission from the System. If the permission is allowed, then the permission is added to
     * the permissions map, however, if declined, a message is displayed to the user
     * @param permission the permission to request
     * @param requestCode the associated request code
     */
    public void requestPermission(String permission, final int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        else
            Toast.makeText(getApplicationContext(), "Permission: " + permission + " not allowed", Toast.LENGTH_LONG).show();
    }

    /**
     * Processes the results of Permission Requests. Permissions are replaced / added as per each permission request.
     * @param requestCode the associated permission request code
     * @param permissions the permissions requested
     * @param grantResults the results of the permissions request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // enumerate all permissions requested
        for (int i = 0; i < permissions.length; i++) {
            // if the Permissions Hash Map contains the requested permission, replace the current grantResult with the new one,
            // otherwise add the permission with the grantResult
            if (getPermissions().containsKey(permissions[i]))
                getPermissions().replace(permissions[i], grantResults[i]);
            else
                getPermissions().put(permissions[i], grantResults[i]);
        }
    }

    /**
     * Getters and Setters
     */
    public Map<String, Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Integer> permissions) {
        this.permissions = permissions;
    }
}
