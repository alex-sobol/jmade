package org.jmade.core.message;

public interface ChannelSender {
    void send(String channelName, String data);
}
