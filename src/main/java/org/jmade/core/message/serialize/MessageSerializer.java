package org.jmade.core.message.serialize;

import org.jmade.core.message.ACLMessage;

public interface MessageSerializer {
    String serialize(ACLMessage message);
    ACLMessage deserialize(String rawMessage);

}
