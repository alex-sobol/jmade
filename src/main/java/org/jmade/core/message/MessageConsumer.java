package org.jmade.core.message;

public interface MessageConsumer extends Stoppable{
    void setMessageReceivedCallback(MessageReceiver callback);
}
