package com.deakin.ghosttransmission.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final int CONV_ACTIV_REQUEST_CODE = 2000; // conversation activity request code

    // GYRO SCREEN
    private final float NORM = 1000000; // 1 million microseconds to 1 second
    private final float RANGE = 5; // number of pixels before snapping the GyroScreen to the closest edge

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

    // SMS MANAGEMENT
    private SMSManager smsManager = null; // sms manager used to execute sms related tasks

    // CONTROLLERS
    private ConversationController conversationController = null; // central point of communication between Conversation and Database

    // ADAPTERS
    private IdentityAdapter identityAdapter = null; // identity adapter for identity recycler view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init permissions arrays
        permissions = new HashMap<>();

        // request permissions required for application
        int read_send_sms_res = requestPermission(new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, REQUEST_CODE);

        // initialize activity
        if (read_send_sms_res > 0)
            initializeActivity();
    }

    private void initializeActivity() {

        // init controllers
        setConversationController(new ConversationController(getApplicationContext()));

        // create sms manager instance
        SMSManager smsManager = new SMSManager(getContentResolver());
        setSmsManager(smsManager);

        // init conversation identity recycler view
        conversationIdentityRV = findViewById(R.id.conversation_identity_recyclerview);

        // set the layout manager for the conversation identity recycler view (linear)
        conversationIdentityRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // set adapter for identities
        setIdentityAdapter(new IdentityAdapter(getUpdatedIdentityList(), this));

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
        gyroScreen.addGyroEventListener();
    }

    /**
     * Requests a single permission from the System. If the permission is allowed, then the permission is added to
     * the permissions map
     *
     * @param permissions the permissions to request
     * @param requestCode the associated request code
     * @return whether the permission has already been granted (>0) or has been requested (0)
     */
    public int requestPermission(String[] permissions, final int requestCode) {
        // create a list of non-granted permissions
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                permissionList.add(permission);

        // return number > 0 if the permissions have already been granted
        if (permissionList.size() == 0)
            return 1;
        else { // return 0 if the permissions have yet to be granted (i.e. need to be requested)
            String[] permissionsToRequest = new String[permissionList.size()];
            permissionsToRequest = permissionList.toArray(permissionsToRequest);
            ActivityCompat.requestPermissions(this, permissionsToRequest, requestCode);
            return 0;
        }
    }

    /**
     * Retrieves an updates list of Identities from the database
     *
     * @return new identity list
     */
    public ArrayList<String> getUpdatedIdentityList() {
        ConversationList cv = new ConversationList(new ArrayList<Conversation>(), getConversationController());
        HashMap<Integer, String> addresses = getSmsManager().getSMSAddressList(SMSURI.INBOX_URI, SMSURI.SENT_URI);
        HashMap<Integer, String> identities = cv.retrieveIdentities(addresses);
        ArrayList<String> identitesAL = new ArrayList<>(identities.values());
        return identitesAL;
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
     * all permissions have been granted, and exits otherwise
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

        // check the access status for each of the above permissions
        for (int i = 0; i < permissions.length; i++) {
            // if any of the permissions have been declined, present a message and finish (exit the application)
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Viewing and Sending SMS Messages must be allowed to use this application", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        // if all permissions have been granted, continue with activity setup
        initializeActivity();
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

        double change = y * (10000.0 / NORM);
        degrees += change;
        float newTransX = -BASE + degrees * SENSITIVITY;

        if (newTransX >= -BASE && newTransX <= 0)
            gyroScreenView.setTranslationX(newTransX);
        else if (newTransX < -BASE && gyroScreenView.getTranslationX() != -BASE) {
            degrees -= change;
            gyroScreenView.setTranslationX(-BASE);
        } else if (newTransX >= 0 && gyroScreenView.getTranslationX() != 0) {
            degrees -= change;
            gyroScreenView.setTranslationX(0);
        }
    }

    /**
     * Change the main window to show the Conversation Activity with the given Identity
     *
     * @param conversationIdentity conversation identity
     */
    @Override
    public void onRequestOpenConversationView(String conversationIdentity) {
        Intent i = new Intent(this, ConversationActivity.class);
        i.putExtra("conversation_identity", conversationIdentity);
        // get the address associated with the conversation identity
        String address = getConversationController().onAddressRequest(conversationIdentity);
        i.putExtra("address", address);
        startActivityForResult(i, CONV_ACTIV_REQUEST_CODE);
    }

    /**
     * Updates the Identity Adapter when the Conversation Activity finalises
     *
     * @param requestCode request code when th Activity was started
     * @param resultCode  resulting code from Activity
     * @param data        intent extra data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is equal to the conversation activity request code
        if (requestCode == CONV_ACTIV_REQUEST_CODE) {

            // update identity recycler view with updated adapter
            getIdentityAdapter().setIdentityList(getUpdatedIdentityList());
            getConversationIdentityRV().swapAdapter(getIdentityAdapter(), false);

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
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

    public RecyclerView getConversationIdentityRV() {
        return conversationIdentityRV;
    }

    public void setConversationIdentityRV(RecyclerView conversationIdentityRV) {
        this.conversationIdentityRV = conversationIdentityRV;
    }

    public View getGyroScreenView() {
        return gyroScreenView;
    }

    public void setGyroScreenView(View gyroScreenView) {
        this.gyroScreenView = gyroScreenView;
    }

    public SMSManager getSmsManager() {
        return smsManager;
    }

    public void setSmsManager(SMSManager smsManager) {
        this.smsManager = smsManager;
    }

    public ConversationController getConversationController() {
        return conversationController;
    }

    public void setConversationController(ConversationController conversationController) {
        this.conversationController = conversationController;
    }

    public IdentityAdapter getIdentityAdapter() {
        return identityAdapter;
    }

    public void setIdentityAdapter(IdentityAdapter identityAdapter) {
        this.identityAdapter = identityAdapter;
    }
}
