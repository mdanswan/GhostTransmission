package com.deakin.ghosttransmission.Controller;

import android.content.Context;

import com.deakin.ghosttransmission.Database.SQLiteDatabaseManager;
import com.deakin.ghosttransmission.Listener.ConversationListener;

public class ConversationController implements ConversationListener {

    /**
     * Instance Variables
     */
    // CONTEXT
    private Context context = null;

    /**
     * Constructor
     *
     * @param context context
     */
    public ConversationController(Context context) {
        setContext(context);
    }

    @Override
    public String OnIdentityRequest(String address) {
        SQLiteDatabaseManager dbManager = new SQLiteDatabaseManager(getContext());
        String identity = dbManager.getIdentity(address);
        return identity;
    }

    /**
     * Getters and Setters
     */
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
