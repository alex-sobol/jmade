package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessageReceiver;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//public class MessageSubscriber<T extends ACLMessage> {
public class MessageSubscriber implements MessageManager, MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    protected String id;
    private MessageProcessor messageProcessor;

    private List<KafkaMessageConsumer> consumers = new ArrayList<>();
    private MessageConverter<ACLMessage> messageConverter = new JsonConverter<>(ACLMessage.class);
    private TopicManager topicManager = new TopicManager();

    public MessageSubscriber(String id) {
        this.id = id;
    }

    public MessageSubscriber() {
    }

    @Override
    public void setMessageProcessor(MessageProcessor callback) {
        this.messageProcessor = callback;
        listenToChannel(id);
    }

    @Override
    public void listenToChannel(String channelName) {
        listenToChannel(channelName, UUID.randomUUID().toString());
    }

    public void listenToChannel(String channelName, String groupName) {
        topicManager.createTopic(channelName);
        KafkaMessageConsumer consumer = new KafkaMessageConsumer(channelName, groupName);
        consumer.setMessageReceivedCallback(this);
        consumers.add(consumer);
    }

    @Override
    public void onMessageReceived(String channelName, String data) {
        try {
            logger.info(id + " received from channel: " + channelName + " - " + data);
            ACLMessage aclMessage = messageConverter.deserialize(data);
            messageProcessor.onMessageReceived(aclMessage);
        } catch (NullPointerException npe) {
            logger.error("NPE in " + id, npe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        consumers.forEach(KafkaMessageConsumer::close);
    }

    @Override
    public void send(String channelName, String data) {

    }

    @Override
    public void respond(ACLMessage message, String data) {

    }
}
