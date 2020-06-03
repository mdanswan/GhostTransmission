package com.deakin.ghosttransmission.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deakin.ghosttransmission.Model.Conversation;
import com.deakin.ghosttransmission.Model.ConversationList;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;

class ConversationViewHolder extends RecyclerView.ViewHolder {

    /**
     * Instance Variables
     */
    private Button conversationIdentityButton = null;

    /**
     * Constructor
     *
     * @param view conversation view layout
     */
    public ConversationViewHolder(@NonNull View view) {
        super(view);
        setConversationIdentityButton((Button) view.findViewById(R.id.conversation_identity_button));
    }

    /**
     * Getters and Setters
     */
    public Button getConversationIdentityButton() {
        return conversationIdentityButton;
    }

    public void setConversationIdentityButton(Button conversationIdentityButton) {
        this.conversationIdentityButton = conversationIdentityButton;
    }
};

public class ConversationAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    /**
     * Instance Variables
     */
    // CONVERSATION LIST
    private ConversationList conversationList = null;

    /**
     * Constructor
     *
     * @param conversationList List of Conversations
     */
    public ConversationAdapter(@NonNull ConversationList conversationList) {
        setConversationList(conversationList);
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View conversationIdentityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_identity_layout, parent, false);
        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(conversationIdentityView);
        conversationViewHolder.getConversationIdentityButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {

        // get the current Conversation model from the conversation dataset
        Conversation conversationIdentity = getConversationList().get(position);

        // set the content of the identity edit text for the Conversation
        Button conversationIdentityButton = holder.getConversationIdentityButton();
        conversationIdentityButton.setText(conversationIdentity.getIdentity());
    }

    @Override
    public int getItemCount() {
        return getConversationList().size();
    }

    /**
     * Getters and Setters
     */
    public ConversationList getConversationList() {
        return conversationList;
    }

    public void setConversationList(ConversationList conversationList) {
        this.conversationList = conversationList;
    }
}
