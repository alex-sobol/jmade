package org.jmade.core.message;

import java.io.Closeable;

public interface MessageConsumer extends Closeable {
    void setMessageReceivedCallback(MessageReceiver callback);
}
