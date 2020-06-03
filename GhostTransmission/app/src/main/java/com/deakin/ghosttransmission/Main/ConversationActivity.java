package com.deakin.ghosttransmission.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.deakin.ghosttransmission.Model.Conversation;
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

        //Conversation conversationIdentity = getIntent().getSerializableExtra("identity");

//        // check if bundle contains conversation data
//        if (conversationIdentity == null) {
//            finish(); // do not proceed without conversation identity data
//            return;
//        }



        initializeUI();
    }

    private void initializeUI()
    {
        // init all UI components
    }
}
