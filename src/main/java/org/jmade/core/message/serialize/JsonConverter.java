package org.jmade.core.message.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmade.core.message.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//todo:make package-private
public class JsonConverter implements MessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    ObjectMapper objectMapper;

    public JsonConverter() {
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
    public ACLMessage deserialize(String rawMessage) {
        ACLMessage aclMessage = null;
        try {
            aclMessage = objectMapper.readValue(rawMessage, ACLMessage.class);
        } catch (IOException e) {
            logger.error("Error parse raw acl message ", e);
        }

        return aclMessage;
    }
}
