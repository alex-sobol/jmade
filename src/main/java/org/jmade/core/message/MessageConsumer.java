package org.jmade.core.message;

// TODO: Consider using some standard interface like Closable
public interface MessageConsumer extends Stoppable{
    void setMessageReceivedCallback(MessageReceiver callback);
}
