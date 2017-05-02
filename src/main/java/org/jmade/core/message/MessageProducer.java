package org.jmade.core.message;

public interface MessageProducer {
    void send(String channelName, String data);
}
