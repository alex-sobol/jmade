package org.jmade.core.message.serialize;

public interface MessageSerializer<T> {
    String serialize(T message);

    T deserialize(String rawMessage);
}
