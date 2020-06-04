package com.deakin.ghosttransmission.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deakin.ghosttransmission.Adapter.ConversationAdapter;
import com.deakin.ghosttransmission.Controller.ConversationController;
import com.deakin.ghosttransmission.Messaging.SMS.SMSManager;
import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.Model.SMSURI;
import com.deakin.ghosttransmission.R;

public class ConversationActivity extends AppCompatActivity {

    /**
     * Instance Variables
     */
    // UI COMPONENTS
    private RecyclerView conversationRV = null;
    private EditText conversationIdentityET = null;
    private EditText messageET = null;
    private ImageButton sendIB = null;
    private ImageButton saveIB = null;

    // SMS MANAGEMENT
    private SMSManager smsManager = null;

    // CONTROLLERS
    private ConversationController conversationController = null;

    // CONVERSATION
    private Conversation conversation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        String conversationIdentity = getIntent().getStringExtra("conversation_identity");
        String address = getIntent().getStringExtra("address");

        // check if bundle contains conversation data
        if (conversationIdentity == null || address == null) {
            finish(); // do not proceed without conversation identity data
            return;
        }

        initializeActivity(conversationIdentity, address);
    }

    private void initializeActivity(String conversationIdentity, String address) {
        // init controllers
        setConversationController(new ConversationController(getApplicationContext()));

        // read conversation list, and update identities
        SMSManager smsManager = new SMSManager(getContentResolver());
        setSmsManager(smsManager);
        smsManager.retrieveConversation(SMSURI.INBOX_URI, SMSURI.SENT_URI, address, -1);
        ConversationList conversationList = smsManager.getConversations();
        conversationList.setConversationController(getConversationController());
        conversationList.updateIdentities();
        setConversation(conversationList.get(0)); // set the internal conversation, as the only conversation in the conversation list

        // init conversation recycler view
        conversationRV = findViewById(R.id.conversation_recyclerview);

        // set the layout manager for the conversation recycler view (linear)
        conversationRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // create adapter for main screen received sms
        ConversationAdapter conversationAdapter = new ConversationAdapter(conversationList.get(0));

        // set main recycler view adapter as the one above
        conversationRV.setAdapter(conversationAdapter);

        // init conversation identity edit text
        conversationIdentityET = findViewById(R.id.conversation_identity_edittext);
        conversationIdentityET.setText(conversationIdentity);

        // init new message edit text
        messageET = findViewById(R.id.message_edittext);

        // init send message button
        sendIB = findViewById(R.id.send_message_imagebutton);
        sendIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMS sms = new SMS(getConversation().getFromAddress(), System.currentTimeMillis(), messageET.getText().toString(), "");
                getSmsManager().sendSMS(sms);

            }
        });

        // init send message button
        saveIB = findViewById(R.id.save_imagebutton);
        saveIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newIdentity = conversationIdentityET.getText().toString();
                getConversationController().onRequestIdentityChange(getConversation().getFromAddress(), newIdentity);
                conversationIdentityET.setText(newIdentity);
                conversationIdentityET.clearFocus();
            }
        });
    }

    /**
     * Getters and Setters
     */
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
