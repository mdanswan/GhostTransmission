package com.deakin.ghosttransmission.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deakin.ghosttransmission.Adapter.SMSAdapter;
import com.deakin.ghosttransmission.Ghosting.GyroScreen;
import com.deakin.ghosttransmission.Listener.GyroscopeListener;
import com.deakin.ghosttransmission.Messaging.SMS.SMSReader;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;
import com.deakin.ghosttransmission.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GyroscopeListener {

    /**
     * Instance Variables
     */
    // DATA
    private Map<String, Integer> permissions = null; // permission to request

    private float degrees = 0;
    private final float NORM = 1000000;
    private float BASE = 0;
    private float SENSITIVITY = 0;

    // UI COMPONENTS
    private RecyclerView smsRV; // sms inbox / sent recycler view
    private View gyroScreenView = null;

    /**
     * Constants
     */
    private final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeUI();

        // init GryoScreen
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        GyroScreen gyroScreen = new GyroScreen(sensorManager, this);
        gyroScreen.AddGyroEventListener();
    }

    public void InitializeUI() {

        // init permissions arrays
        permissions = new HashMap<>();

        // request permissions required for application
        requestPermission(Manifest.permission.READ_SMS, REQUEST_CODE);

        // read sms messages from inbox
        SMSReader smsReader = new SMSReader(getContentResolver());
        ArrayList<SMS> smsList = smsReader.ReadSMS(SMSURI.INBOX_URI, SMSURI.SENT_URI);

        // init sms recycler view
        smsRV = findViewById(R.id.sms_recyclerview);

        // set the layout manager for the sms recycler view (linear)
        smsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // create adapter for main screen received sms
        SMSAdapter smsAdapter = new SMSAdapter(smsList);

        // set main recycler view adapter as the one above
        smsRV.setAdapter(smsAdapter);

        // init BASE constant (cannot change past this point)
        BASE = getWindowWidth();

        // init SENSITIVITY constant
        SENSITIVITY = (360 / BASE) * 45;

        // init gryo screen
        gyroScreenView = findViewById(R.id.gyroscreen_progressbar);
        gyroScreenView.setTranslationX(getWindowWidth());
    }

    /**
     * Requests a single permission from the System. If the permission is allowed, then the permission is added to
     * the permissions map
     *
     * @param permission  the permission to request
     * @param requestCode the associated request code
     */
    public void requestPermission(String permission, final int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    /**
     * Retrieves the Width of the current Window
     *
     * @return Width of the Window
     */
    public int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * Processes the results of Permission Requests. Permissions are replaced / added as per each permission request.
     *
     * @param requestCode  the associated permission request code
     * @param permissions  the permissions requested
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
     * Gyroscope Listener implementation
     *
     * @param x rate of change in x
     * @param y rate of change in y
     * @param z rate of change in z
     */
    @Override
    public void onGyroChange(float x, float y, float z) {

        degrees += y * (10000.0 / NORM);
        float newTransX = BASE - degrees * SENSITIVITY;

        if (newTransX >= 5f && newTransX <= BASE - 5f)
            gyroScreenView.setTranslationX(newTransX);
        else if (newTransX <= 5f && gyroScreenView.getTranslationX() != 0)
            gyroScreenView.setTranslationX(0);
        else if (newTransX >= BASE - 5f && gyroScreenView.getTranslationX() != BASE)
            gyroScreenView.setTranslationX(BASE);
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
