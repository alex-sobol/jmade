package org.jmade.core.message;


public interface MessageManager extends Stoppable {
    void setMessageProcessor(MessageProcessor callback);
    void listenToChannel(String channel);
    //todo: data should be object
    void respond(ACLMessage message, String data);
    void send(String channelName, String data);
}
