package org.jmade.core.message.serialize;

public interface MessageConverter<T> {
    String serialize(T message);

    T deserialize(String rawMessage);
}
