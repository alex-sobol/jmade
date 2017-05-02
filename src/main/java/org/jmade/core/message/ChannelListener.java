package org.jmade.core.message;

public interface ChannelListener extends Stoppable{
    void setMessageReceivedCallback(MessageReceiver callback);
}
