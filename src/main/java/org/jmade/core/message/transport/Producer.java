package org.jmade.core.message.transport;

public interface Producer {
    void send(String channelName, String data);
}
