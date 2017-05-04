package org.jmade.core.message.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//todo:make package-private
public class JsonConverter<T> implements MessageConverter<T> {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    ObjectMapper objectMapper;
    Class<T> type;

    public JsonConverter(Class<T> type) {
        this.type = type;
        objectMapper = new ObjectMapper();
    }

    @Override
    public String serialize(T message) {
        String rawMessage = null;
        try {
            rawMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            logger.error("Error serialize acl message ", e);
        }

        return rawMessage;
    }

    @Override
    public T deserialize(String rawMessage) {
        T aclMessage = null;
        try {
            aclMessage = objectMapper.readValue(rawMessage, type);
        } catch (IOException e) {
            logger.error("Error parse raw acl message ", e);
        }

        return aclMessage;
    }
}
