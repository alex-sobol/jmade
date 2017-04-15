package org.jmade.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yammer.metrics.core.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageManager implements HighLevelMessageConsumer, Stoppable{

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private static final String BROADCAST_CHANNEL = "broadcast";

    private String id;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private MessageProcessor messageProcessor;
    private ObjectMapper objectMapper;

    public MessageManager(String id, MessageProcessor messageProcessor) {
        this.id = id;
        producer = new MessageProducer(id);
        consumer = new MessageConsumer(id, this);
        this.messageProcessor = messageProcessor;
        objectMapper = new ObjectMapper();
    }

    public void broadcast(String data) {
        try {
            ACLMessage aclMessage = new ACLMessage(id, data);
            String rawMessage = objectMapper.writeValueAsString(aclMessage);
            producer.send(BROADCAST_CHANNEL, rawMessage);
        } catch (JsonProcessingException e) {
            logger.error("Error serialize raw acl message ", e);
        }
    }

    public void respond(ACLMessage message, String data) {
        try {
            ACLMessage aclMessage = new ACLMessage(id, data);
            String rawMessage = objectMapper.writeValueAsString(aclMessage);
            producer.send(message.getSenderId(), rawMessage);
        } catch (JsonProcessingException e) {
            logger.error("Error serialize raw acl message ", e);
        }
    }

    @Override
    public void onMessageReceived(String rawMessage) {
        try {
            ACLMessage message = objectMapper.readValue(rawMessage, ACLMessage.class);
            messageProcessor.onMessageReceived(message);
        } catch (IOException e) {
            logger.error("Error parse raw acl message ", e);
        } catch (NullPointerException npe){
            logger.error("NPE" + id);
        }
    }

    @Override
    public void stop() {
        consumer.stop();
    }
}
