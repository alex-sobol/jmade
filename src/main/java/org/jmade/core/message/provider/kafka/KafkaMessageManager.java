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
import java.util.ArrayList;
import java.util.List;

public class KafkaMessageManager implements MessageManager, MessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageManager.class);

    private String id;
    private MessageProcessor messageProcessor;

    private KafkaMessageProducer producer;
    private List<KafkaMessageConsumer> consumers = new ArrayList<>();

    private MessageSerializer<ACLMessage> messageSerializer = new JsonSerializer();
    private TopicManager topicManager = new TopicManager();


    public KafkaMessageManager(String id) {
        this.id = id;
        this.producer = new KafkaMessageProducer();
    }

    @Override
    public void setMessageProcessor(MessageProcessor callback){
        this.messageProcessor = callback;
        listenToChannel(id);
    }

    @Override
    public void listenToChannel(String channelName) {
        topicManager.createTopic(channelName);
        KafkaMessageConsumer consumer = new KafkaMessageConsumer(channelName);
        consumer.setMessageReceivedCallback(this);
        consumers.add(consumer);
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
    public void send(String channelName, String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);
        String rawMessage = messageSerializer.serialize(aclMessage);
        if (rawMessage != null) {
            logger.debug(channelName + " broadcasts: " + data);
            producer.send(channelName, rawMessage);
        }
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

    @Override
    public void stop() {
        topicManager.deleteTopic(id);
        consumers.forEach(KafkaMessageConsumer::stop);
    }

    //todo:add enable logs.
    // Send and receive messsages - different case. MessageLog(type(sent-received), actorId, time, message)
}
