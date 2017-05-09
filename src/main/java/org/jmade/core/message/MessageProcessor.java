package org.jmade.core.message;

//todo: remove from all highlevel classes
public interface MessageProcessor {
    void onMessageReceived(ACMessage message);
}
