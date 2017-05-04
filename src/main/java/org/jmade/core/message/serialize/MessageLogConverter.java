package org.jmade.core.message.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmade.logs.persistence.model.MessageLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//todo:serializers become generic
public class MessageLogConverter implements MessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(MessageLogConverter.class);

    ObjectMapper objectMapper;

    public MessageLogConverter() {
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
            // TODO: Look for a way to get generic class from class signature
            aclMessage = objectMapper.readValue(rawMessage, MessageLog.class);
        } catch (IOException e) {
            logger.error("Error parse raw acl message ", e);
        }

        return aclMessage;
    }
}
