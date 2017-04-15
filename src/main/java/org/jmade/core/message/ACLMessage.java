package org.jmade.core.message;

public class ACLMessage {

    private  String senderId;
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
