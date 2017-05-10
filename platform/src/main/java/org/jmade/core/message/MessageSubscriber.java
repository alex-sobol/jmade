package org.jmade.core.message;

import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.core.message.transport.Consumer;
import org.jmade.core.message.transport.ConsumerCallback;
import org.jmade.core.message.transport.provider.kafka.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//public class MessageSubscriber<T extends ACLMessage> {
public class MessageSubscriber implements ConsumerCallback {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    protected String id;
    private List<Consumer> consumers;
    private MessageConverter<ACMessage> converter;

    private MessageProcessor messageProcessor;

    public MessageSubscriber(String id) {
        this.id = id;
        this.consumers = new ArrayList<>();
        this.converter = new JsonConverter<>(ACMessage.class);
    }

    public void setMessageProcessor(MessageProcessor callback) {
        this.messageProcessor = callback;
    }

    public void listenToChannel(String channelName) {
        listenToChannel(channelName, UUID.randomUUID().toString());
    }

    public void listenToChannel(String channelName, String groupName) {
        KafkaConsumer consumer = new KafkaConsumer(channelName, groupName);
        consumer.setMessageReceivedCallback(this);
        consumers.add(consumer);
    }

    @Override
    public void onMessageReceived(String channelName, String data) {
        logger.info(id + " received from channel: " + channelName + " - " + data);
        ACMessage ACMessage = converter.deserialize(data);
        messageProcessor.onMessageReceived(ACMessage);

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
