package com.deakin.ghosttransmission.Model;

import com.deakin.ghosttransmission.Controller.ConversationController;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConversationList extends ArrayList<Conversation> {

    /**
     * Instance Variables
     */
    private ConversationController conversationController = null;

    /**
     * Default Constructor
     */
    public ConversationList() {

    }

    /**
     * Constructor
     *
     * @param conversationsList conversation list
     */
    public ConversationList(ArrayList<Conversation> conversationsList) {
        addAll(conversationsList);
    }

    /**
     * Constructor
     *
     * @param conversationsList      conversation list
     * @param conversationController conversation controller
     */
    public ConversationList(ArrayList<Conversation> conversationsList, ConversationController conversationController) {
        super(conversationsList);
        setConversationController(conversationController);
    }

    /**
     * Updates the Identity of each of the internal Conversations
     *
     * @returns whether the update was successful
     */
    public ArrayList<String> retrieveIdentities(ArrayList<String> addresses) {
        ArrayList<String> addressIdentities = new ArrayList<>();

        // check if the conversation controller is null
        if (getConversationController() == null)
            return addressIdentities;

        // retrieve the identity of all addresses
        for (int i = 0; i < addresses.size(); i++) {
            addressIdentities.add(getConversationController().OnIdentityRequest(addresses.get(i)));
        }

        return addressIdentities;
    }

    /**
     * Getters and Setters
     */
    public ConversationController getConversationController() {
        return conversationController;
    }

    public void setConversationController(ConversationController conversationController) {
        this.conversationController = conversationController;
    }
}
