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

import java.io.IOException;

public class KafkaMessageManager implements MessageManager {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageManager.class);

    private static final String BROADCAST_CHANNEL = "broadcast";

    private String id;
    private MessageProducer producer;
    //TODO: think over concept - onBroadCastReceived + onPrivateReceived or refactor
    private MessageConsumer consumer;
    private MessageConsumer broadCastConsumer;
    private MessageProcessor messageProcessor;
    private MessageSerializer<ACLMessage> messageSerializer;

    public KafkaMessageManager(String id, MessageProcessor messageProcessor) {
        this.id = id;
        setMessageReceivedListener(messageProcessor);
        producer = new MessageProducer();
        if (messageProcessor != null) {
            MessageListener<Integer, String> listener = getListener();
            consumer = new MessageConsumer(false, id, listener);
            broadCastConsumer = new MessageConsumer(true, BROADCAST_CHANNEL, listener);
        }
        messageSerializer = new JsonSerializer();
    }

    @Override
    public void setMessageReceivedListener(MessageProcessor messageReceivedListener) {
        this.messageProcessor = messageReceivedListener;
    }

    @Override
    public void broadcast(String data) {
        ACLMessage aclMessage = new ACLMessage(BROADCAST_CHANNEL, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            logger.debug(id + " broadcasts: " + data);
            producer.send(BROADCAST_CHANNEL, rawMessage);
        }
    }

    @Override
    public void respond(ACLMessage message, String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            logger.debug(id + " responds to:" + message.getSenderId() + " - " + data);
            producer.send(message.getSenderId(), rawMessage);
        }
    }

    @Override
    public void send(String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            logger.debug(id + " broadcasts: " + data);
            producer.send(id, rawMessage);
        }
    }

    private MessageListener getListener() {
        return new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                try {
                    logger.info(id + " received from channel: " + message.topic() + " - " + message.value());
                    ACLMessage aclMessage = messageSerializer.deserialize(message.value());
                    messageProcessor.onMessageReceived(aclMessage);
                } catch (NullPointerException npe) {
                    logger.error("NPE in " + id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    @Override
    public void stop() {
        consumer.stop();
    }
}
