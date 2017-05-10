package org.jmade.core.message.transport;

import java.io.Closeable;

public interface Consumer extends Closeable {
    void setMessageReceivedCallback(ConsumerCallback callback);
}
