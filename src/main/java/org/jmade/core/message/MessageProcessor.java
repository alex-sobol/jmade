package org.jmade.core.message;

public interface MessageProcessor {
    void onMessageReceived(ACLMessage message);
}
