package org.jmade.core.message.provider.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.serialize.JsonSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class KafkaMessageManager implements MessageManager {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private static final String BROADCAST_CHANNEL = "broadcast";

    private String id;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private MessageProcessor messageProcessor;
    private MessageSerializer messageSerializer;

    public KafkaMessageManager(String id, MessageProcessor messageProcessor) {
        this.id = id;
        producer = new MessageProducer(id);
        consumer = new MessageConsumer(id, getListener());
        messageSerializer = new JsonSerializer();
        setMessageReceivedListener(messageProcessor);
    }

    @Override
    public void broadcast(String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            producer.send(BROADCAST_CHANNEL, rawMessage);
        }
    }

    @Override
    public void respond(ACLMessage message, String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            producer.send(message.getSenderId(), rawMessage);
        }
    }

    @Override
    public void setMessageReceivedListener(MessageProcessor messageReceivedListener) {
        this.messageProcessor = messageReceivedListener;
    }

    private MessageListener getListener() {
        return new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                try {
                    logger.info(message.topic() + " received: " + message.value());
                    ACLMessage aclMessage = messageSerializer.deserialize(message.value());
                    messageProcessor.onMessageReceived(aclMessage);
                } catch (NullPointerException npe) {
                    logger.error("NPE" + id);
                }
            }

        };
    }

    @Override
    public void stop() {
        consumer.stop();
    }
}
