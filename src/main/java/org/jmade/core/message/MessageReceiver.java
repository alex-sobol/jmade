package org.jmade.core.message;

@FunctionalInterface
public interface MessageReceiver {
    void onMessageReceived(String channelName, String data);
}
