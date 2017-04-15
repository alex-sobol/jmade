package org.jmade.core.message;

import com.yammer.metrics.core.Stoppable;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

public class MessageConsumer implements Stoppable{
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private static final String GROUP = "group";
    private String topic;

    private KafkaMessageListenerContainer<Integer, String> container;
    private HighLevelMessageConsumer messageConsumer;

    TopicManager topicManager = new TopicManager();

    public MessageConsumer(String topic, HighLevelMessageConsumer listener) {
        this.topic = topic;
        messageConsumer = listener;
        initContainer(topic);
    }

    private KafkaMessageListenerContainer initContainer(String topic1) {
        topicManager.createTopic(topic1);
        container = createContainer(topic1);
        container.setupMessageListener(getListener());
        container.start();

        return container;
    }

    private MessageListener getListener(){
        return new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                logger.info(message.topic() + " received: " + message.value());
                messageConsumer.onMessageReceived(message.value());
            }

        };
    }


    private KafkaMessageListenerContainer<Integer, String> createContainer(String topic1) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<Integer, String>(props);
        ContainerProperties containerProperties = new ContainerProperties(topic1);
        KafkaMessageListenerContainer<Integer, String> container =
                new KafkaMessageListenerContainer<>(cf, containerProperties);
        return container;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    @Override
    public void stop() {
        container.stop();
        topicManager.deleteTopic(topic);
    }
}
