package org.jmade.core.message;

import java.io.IOException;

public interface MessageProcessor {
    void onMessageReceived(ACLMessage message) throws IOException;
}
