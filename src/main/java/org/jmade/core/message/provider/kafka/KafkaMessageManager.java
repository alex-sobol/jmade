package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessageReceiver;
import org.jmade.core.message.serialize.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Split into two different implementations
public class KafkaMessageManager implements MessageManager, MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageManager.class);

    protected String id;
    private MessageProcessor messageProcessor;

    protected KafkaMessageProducer producer;
    private List<KafkaMessageConsumer> consumers = new ArrayList<>();

    protected JsonConverter<ACLMessage> messageConverter = new JsonConverter<>(ACLMessage.class);
    private TopicManager topicManager = new TopicManager();


    public KafkaMessageManager(String id) {
        this.id = id;
        this.producer = new KafkaMessageProducer();
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
    public void respond(ACLMessage message, String data) {
        send(message.getSenderId(), data);
    }

    // TODO: Having consumer and producer in one place is not the best idea
    @Override
    public void send(String channelName, String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageConverter.serialize(aclMessage);
        if (rawMessage != null) {
            logger.debug(channelName + " sends: " + data);
            producer.send(channelName, rawMessage);
        }
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
    public void stop() {
        consumers.forEach(KafkaMessageConsumer::stop);
    }

    //todo:add enable logs.
    // Send and receive messsages - different case. MessageLog(type(sent-received), actorId, time, message)
}
