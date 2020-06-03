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
    public boolean updateIdentities() {
        // check if the conversation controller is null
        if (getConversationController() == null)
            return false;

        // update the Identity of all internal Conversations
        for (int i = 0; i < size(); i++) {
            Conversation c = get(i);
            c.setIdentity(getConversationController().OnIdentityRequest(c.getFromAddress()));
        }

        return true;
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
