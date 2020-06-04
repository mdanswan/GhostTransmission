package com.deakin.ghosttransmission.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.deakin.ghosttransmission.Adapter.IdentityAdapter;
import com.deakin.ghosttransmission.Controller.ConversationController;
import com.deakin.ghosttransmission.Ghosting.Visual.GyroScreen;
import com.deakin.ghosttransmission.Listener.GyroscopeListener;
import com.deakin.ghosttransmission.Listener.ViewListener;
import com.deakin.ghosttransmission.Messaging.SMS.SMSManager;
import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.Model.SMSURI;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements GyroscopeListener, ViewListener {

    /**
     * Constants
     */
    // PERMISSIONS
    private final int REQUEST_CODE = 1000; // permission request code

    // GYRO SCREEN
    private final float NORM = 1000000; // 1 million microseconds to 1 second

    /**
     * Instance Variables
     */
    // PERMISSIONS
    private Map<String, Integer> permissions = null; // permission to request

    // GYRO SCREEN
    private float degrees = 0; // current rotation in degrees
    private float BASE = 0; // base of Gyro Screen calculations (i.e. origin)
    private float SENSITIVITY = 0; // sensitivity of change in position of Gyro Screen

    // UI COMPONENTS
    private RecyclerView conversationIdentityRV; // conversation identity Recycler View
    private View gyroScreenView = null; // gyro screen cover

    // CONTROLLERS
    private ConversationController conversationController = null; // central point of communication between Conversation and Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init permissions arrays
        permissions = new HashMap<>();

        // request permissions required for application
        int result = requestPermission(Manifest.permission.READ_SMS, REQUEST_CODE);

        // initialize activity
        if (result > 0)
            initializeActivity();
    }

    private void initializeActivity() {

        // init controllers
        setConversationController(new ConversationController(getApplicationContext()));

        // read identity list
        ConversationList cv = new ConversationList(new ArrayList<Conversation>(), getConversationController());
        SMSManager smsManager = new SMSManager(getContentResolver());
        ArrayList<String> addresses = smsManager.getSMSAddressList(SMSURI.INBOX_URI, SMSURI.SENT_URI);
        ArrayList<String> identities = cv.retrieveIdentities(addresses);

//        ConversationList conversationList = smsManager.getConversations();
//        conversationList.setConversationController(getConversationController());
//        conversationList.updateIdentities();

        // init conversation identity recycler view
        conversationIdentityRV = findViewById(R.id.conversation_identity_recyclerview);

        // set the layout manager for the conversation identity recycler view (linear)
        conversationIdentityRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // create adapter for identities
        IdentityAdapter identityAdapter = new IdentityAdapter(identities, this);

        // set main recycler view adapter as the one above
        conversationIdentityRV.setAdapter(identityAdapter);

        // init gryo screen
        gyroScreenView = findViewById(R.id.gyroscreen_progressbar);
        gyroScreenView.setTranslationX(getWindowWidth());

        // set BASE constant (representing the window size)
        BASE = getWindowWidth();
        // set SENSITIVITY constant (representing the scale of movement for the Gyro Screen
        SENSITIVITY = (360 / BASE) * 45;

        // init GyroScreen
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        GyroScreen gyroScreen = new GyroScreen(sensorManager, this);
        gyroScreen.AddGyroEventListener();
    }

    /**
     * Requests a single permission from the System. If the permission is allowed, then the permission is added to
     * the permissions map
     *
     * @param permission  the permission to request
     * @param requestCode the associated request code
     * @return whether the permission has already been granted (>0) or has been requested (0)
     */
    public int requestPermission(String permission, final int requestCode) {
        // return 0 if the permissions have yet to be granted (i.e. need to be requested)
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return 0;
        } else // return number > 0 if the permissions have already been granted
            return 1;
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
     * Processes the results of Permission Requests. Permissions are replaced / added as per each permission request. The Activity is Initialized if
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

        // if SMS access has been granted, initialize the relevant components in the activity
        if (getPermissions().containsKey(Manifest.permission.READ_SMS) &&
                getPermissions().get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            initializeActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Access to SMS Message must be allowed to use this application", Toast.LENGTH_LONG).show();
            finish();
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

    @Override
    public void OnRequestOpenConversationView(String conversationIdentity) {
        Intent i = new Intent(this, ConversationActivity.class);
        i.putExtra("conversation_identity", conversationIdentity);
        startActivity(i);
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

    public ConversationController getConversationController() {
        return conversationController;
    }

    public void setConversationController(ConversationController conversationController) {
        this.conversationController = conversationController;
    }
}
