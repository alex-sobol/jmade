package org.jmade.core.message.provider.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jmade.core.message.ChannelListener;
import org.jmade.core.message.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KafkaChannelListener implements ChannelListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaChannelListener.class);

    private String topic;
    private boolean isBroadcast;
    private String groupName;
    private KafkaMessageListenerContainer<Integer, String> container;
    private TopicManager topicManager;

    public KafkaChannelListener(String topic, Boolean isBroadcast, String groupName) {
        this.topic = topic;
        this.isBroadcast = isBroadcast;
        this.groupName = groupName;
        this.topicManager = new TopicManager();
    }

    public KafkaChannelListener(String topic, Boolean isBroadcast) {
        this(topic, isBroadcast, UUID.randomUUID().toString());
    }

    @Override
    public void setMessageReceivedCallback(MessageReceiver callback) {
        topicManager.createTopic(topic);
        container = createContainer();
        container.setupMessageListener(getListener(callback));
        container.start();
    }

    private KafkaMessageListenerContainer<Integer, String> createContainer() {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<>(props);
        ContainerProperties containerProperties = new ContainerProperties(topic);

        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    private MessageListener getListener(MessageReceiver callback) {
        return new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                callback.onMessageReceived(message.topic(), message.value());
            }
        };
    }

    @Override
    public void stop() {
        container.stop();
        if(!isBroadcast) {
            topicManager.deleteTopic(topic);
        }
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        /* props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);*/
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("auto.offset.reset", "latest");
        return props;
    }
}
