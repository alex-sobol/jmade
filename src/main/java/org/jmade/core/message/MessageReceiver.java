package org.jmade.core.message;


public interface MessageReceiver {
    void onMessageReceived(String channelName, String data);
}
