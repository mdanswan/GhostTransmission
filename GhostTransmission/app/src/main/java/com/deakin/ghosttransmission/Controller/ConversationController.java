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

    // DATABASE MANAGEMENT
    private SQLiteDatabaseManager sqLiteDatabaseManager = null;

    /**
     * Constructor
     *
     * @param context context
     */
    public ConversationController(Context context) {
        setContext(context);
        setSqLiteDatabaseManager(new SQLiteDatabaseManager(getContext()));
    }

    /**
     * Retrieves the Identity associated with the given Address
     *
     * @param address address with an associated identity
     * @return identity of address
     */
    @Override
    public String onIdentityRequest(String address) {
        String identity = getSqLiteDatabaseManager().getIdentity(address);
        return identity;
    }

    /**
     * Retrieves the Address associated with the given Identity
     *
     * @param identity identity with an associated address
     * @return address of identity
     */
    @Override
    public String onAddressRequest(String identity) {
        String address = getSqLiteDatabaseManager().getAddress(identity);
        return address;
    }

    @Override
    public void onRequestIdentityChange(String address, String newIdentity) {
        getSqLiteDatabaseManager().updateIdentity(address, newIdentity);
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

    public SQLiteDatabaseManager getSqLiteDatabaseManager() {
        return sqLiteDatabaseManager;
    }

    public void setSqLiteDatabaseManager(SQLiteDatabaseManager sqLiteDatabaseManager) {
        this.sqLiteDatabaseManager = sqLiteDatabaseManager;
    }
}
