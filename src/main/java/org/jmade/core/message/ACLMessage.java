package org.jmade.core.message;

import org.jmade.example.dto.TradeRequest;

//todo: consider whether backwards compatibility and versions of the messages are required
public class ACLMessage {
    //private  String discriminator;
    private  String senderId;
    //todo: think about map and client util to map
    private  String content;

    public ACLMessage() {
    }

    public ACLMessage(String senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
