package com.deakin.ghosttransmission.Listener;

public interface ConversationListener {
    String onIdentityRequest(String address);

    String onAddressRequest(String identity);

    void onRequestIdentityChange(String address, String newIdentity);
}
