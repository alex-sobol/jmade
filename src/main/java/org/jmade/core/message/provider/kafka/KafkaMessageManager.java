package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessageReceiver;
import org.jmade.core.message.serialize.JsonSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KafkaMessageManager implements MessageManager, MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageManager.class);

    private static final String BROADCAST_CHANNEL = "broadcast";

    private String id;
    private KafkaChannelSender producer;
    //TODO: think over concept - onBroadCastReceived + onPrivateReceived or refactor
    private KafkaChannelListener consumer;
    private KafkaChannelListener broadCastConsumer;
    private MessageProcessor messageProcessor;
    private MessageSerializer<ACLMessage> messageSerializer;

    public KafkaMessageManager(String id, MessageProcessor messageProcessor) {
        this.id = id;
        setMessageReceivedListener(messageProcessor);
        producer = new KafkaChannelSender();
        if (messageProcessor != null) {
            consumer = new KafkaChannelListener(id, false);
            consumer.setMessageReceivedCallback(this);
            broadCastConsumer = new KafkaChannelListener(BROADCAST_CHANNEL, true);
            broadCastConsumer.setMessageReceivedCallback(this);
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

    @Override
    public void stop() {
        consumer.stop();
    }

    @Override
    public void onMessageReceived(String channelName, String data) {
        try {
            logger.info(id + " received from channel: " + channelName + " - " + data);
            ACLMessage aclMessage = messageSerializer.deserialize(data);
            messageProcessor.onMessageReceived(aclMessage);
        } catch (NullPointerException npe) {
            logger.error("NPE in " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
