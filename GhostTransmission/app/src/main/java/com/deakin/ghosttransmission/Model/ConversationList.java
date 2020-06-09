package com.deakin.ghosttransmission.Model;

import com.deakin.ghosttransmission.Controller.ConversationController;

import java.util.ArrayList;
import java.util.HashMap;

public class ConversationList extends ArrayList<Conversation> {

    /**
     * Instance Variables
     */
    private ConversationController conversationController = null;
    private ArrayList<SMS> mergedConversation = null;

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
     * Retrieves the Identities of the given addresses
     */
    public HashMap<Integer, String> retrieveIdentities(HashMap<Integer, String> addresses) {
        HashMap<Integer, String> addressIdentities = new HashMap<>();

        // check if the conversation controller is null
        if (getConversationController() == null)
            return addressIdentities;

        // retrieve the identity of all addresses
        for (int i = 0; i < addresses.size(); i++)
            addressIdentities.put(i, getConversationController().onIdentityRequest(addresses.get(i)));

        return addressIdentities;
    }

    /**
     * Updates the Identity of each of the internal Conversations
     */
    public void updateIdentities() {
        // get a list of the internal conversation addresses
        HashMap<Integer, String> addresses = new HashMap<>();
        for (int i = 0; i < size(); i++)
            addresses.put(i, get(i).getFromAddress());

        // retrieve and set the identities of the internal addresses
        HashMap<Integer, String> identities = retrieveIdentities(addresses);
        for (int i = 0; i < identities.size(); i++)
            get(i).setIdentity(identities.get(i));
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

    public ArrayList<SMS> getMergedConversation() {
        return mergedConversation;
    }

    public void setMergedConversation(ArrayList<SMS> mergedConversation) {
        this.mergedConversation = mergedConversation;
    }
}
