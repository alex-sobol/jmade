package org.jmade.core.message;

import java.io.IOException;

//todo: remove from all highlevel classes
public interface MessageProcessor {
    void onMessageReceived(ACLMessage message) throws IOException;
}
