package org.jmade.core.message;


import java.io.Closeable;

public interface MessageManager extends Closeable {
    void setMessageProcessor(MessageProcessor callback);

    void listenToChannel(String channel);

    //todo: data should be object
    void respond(ACLMessage message, String data);

    void send(String channelName, String data);
}
