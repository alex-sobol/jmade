package org.jmade.core.message;

public interface MessageProcessor {
    void onMessageReceived(ACMessage message);
}
