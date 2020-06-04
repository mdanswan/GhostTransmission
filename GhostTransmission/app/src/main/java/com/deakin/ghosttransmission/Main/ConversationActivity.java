package com.deakin.ghosttransmission.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.deakin.ghosttransmission.Messaging.SMS.SMSManager;
import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.Model.SMSURI;
import com.deakin.ghosttransmission.R;

public class ConversationActivity extends AppCompatActivity {

    /**
     * Instance Variables
     */
    // UI COMPONENTS
    private RecyclerView conversationRV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        String conversationIdentity = getIntent().getStringExtra("conversation_identity");

        // check if bundle contains conversation data
        if (conversationIdentity == null) {
            finish(); // do not proceed without conversation identity data
            return;
        }

        initializeActivity();
    }

    private void initializeActivity()
    {
//        // read conversation list
//        SMSManager smsManager = new SMSManager(getContentResolver());
//        smsManager.retrieveConversations(SMSURI.INBOX_URI, SMSURI.SENT_URI);
//        ConversationList conversationList = smsManager.getConversations();
//        conversationList.setConversationController(getConversationController());
//        conversationList.updateIdentities();
//
//        // init conversation recycler view
//        conversationRV = findViewById(R.id.conversation_identity_recyclerview);
//
//        // set the layout manager for the conversation recycler view (linear)
//        conversationRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        // create adapter for main screen received sms
//         conversationAdapter = new ConversationAdapter(conversationList, this);
//
//        // set main recycler view adapter as the one above
//        conversationRV.setAdapter(conversationAdapter);
    }
}
