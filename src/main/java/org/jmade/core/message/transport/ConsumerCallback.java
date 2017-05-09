package org.jmade.core.message.transport;

@FunctionalInterface
public interface ConsumerCallback {
    void onMessageReceived(String channelName, String data);
}
