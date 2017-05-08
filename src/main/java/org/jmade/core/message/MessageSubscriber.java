package org.jmade.core.message;

import org.jmade.core.message.*;
import org.jmade.core.message.provider.kafka.KafkaMessageConsumer;
import org.jmade.core.message.provider.kafka.TopicManager;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//public class MessageSubscriber<T extends ACLMessage> {
public class MessageSubscriber implements MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    protected String id;
    private MessageProcessor messageProcessor;

    private List<MessageConsumer> consumers = new ArrayList<>();
    private MessageConverter<ACLMessage> messageConverter = new JsonConverter<>(ACLMessage.class);
    private TopicManager topicManager = new TopicManager();

    public MessageSubscriber(String id) {
        this.id = id;
    }

    public MessageSubscriber() {
    }

    public void setMessageProcessor(MessageProcessor callback) {
        this.messageProcessor = callback;
        listenToChannel(id);
    }

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

    public void close() {
        consumers.forEach((kafkaMessageConsumer) -> {
            try {
                kafkaMessageConsumer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
