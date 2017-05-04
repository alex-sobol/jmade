package org.jmade.core.message.serialize;

import org.jmade.core.message.ACLMessage;

// TODO: Rename to MessageConverter
public interface MessageSerializer<T> {
    String serialize(T message);

    T deserialize(String rawMessage);
}
