package org.jmade.core.message.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmade.logs.persistence.model.MessageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageLogSerializer implements MessageSerializer {

    private static final Logger logger = LoggerFactory.getLogger(MessageLogSerializer.class);

    ObjectMapper objectMapper;

    public MessageLogSerializer() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public String serialize(Object message) {
        String rawMessage = null;
        try {
            rawMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Error serialize acl message ", e);
        }

        return rawMessage;
    }

    @Override
    public MessageLog deserialize(String rawMessage) {
        MessageLog aclMessage = null;
        try {
            aclMessage = objectMapper.readValue(rawMessage, MessageLog.class);
        } catch (IOException e) {
            logger.error("Error parse raw acl message ", e);
        }

        return aclMessage;
    }
}
