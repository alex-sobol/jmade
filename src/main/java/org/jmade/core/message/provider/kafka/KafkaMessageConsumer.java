package org.jmade.core.message.provider.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jmade.core.message.MessageConsumer;
import org.jmade.core.message.MessageReceiver;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

public class KafkaMessageConsumer implements MessageConsumer {
    private String topic;
    private String groupName;
    private KafkaMessageListenerContainer<Integer, String> container;

    public KafkaMessageConsumer(String topic, String groupName) {
        this.topic = topic;
        this.groupName = groupName;
    }

    @Override
    public void setMessageReceivedCallback(MessageReceiver callback) {
        if (container != null) {
            container.stop();
        }
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
    public void close() {
        container.stop();
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
