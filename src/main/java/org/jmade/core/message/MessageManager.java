package org.jmade.core.message;


public interface MessageManager extends Stoppable {
    void broadcast(String data);
    void respond(ACLMessage message, String data);
    void setMessageReceivedListener(MessageProcessor messageReceivedListener);
}
