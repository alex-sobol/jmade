package org.jmade;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.jmade.core.message.MessageConsumer;
import org.jmade.core.message.MessageProducer;
import org.jmade.core.message.TopicManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestProducerConsumerManualCreation {

    private static final Logger logger = LoggerFactory.getLogger(TestProducerConsumerManualCreation.class);

    private static final String topic1 = "broadcast";
    private static final String topic2 = "broadcast3";
    private static final String group = "group";

    final CountDownLatch latch = new CountDownLatch(4);

    @Test
    public void test() throws Exception {
        new TopicManager().createTopic(topic2);
        KafkaMessageListenerContainer<Integer, String> container = initContainer(topic1);
        KafkaMessageListenerContainer<Integer, String> container1 = initContainer(topic2);
        Thread.sleep(1000); // wait a bit for the container to start
        KafkaTemplate<Integer, String> template = createTemplate(topic1);
        KafkaTemplate<Integer, String> template2 = createTemplate(topic2);
        template.send(topic1, "foo");
        template2.send(topic2, "bar");
        template2.send(topic2 , "baz");
        template.send(topic1, "qux");
        template.flush();
        assertTrue(latch.await(60, TimeUnit.SECONDS));
        container.stop();
        container1.stop();
        logger.info("Stop auto");


    }


    @Test
    public void shortTest() throws InterruptedException {
        String topic1 = "top1";
        String topic2 = "top2";
        TopicManager topicManager = new TopicManager();
        topicManager.createTopic(topic1);
        topicManager.createTopic(topic2);

        MessageConsumer messageConsumer = new MessageConsumer(topic1);
        MessageConsumer messageConsumer1 = new MessageConsumer(topic2);
        Thread.sleep(5000);

        MessageProducer messageProducer = new MessageProducer(topic1);
        MessageProducer messageProducer1 = new MessageProducer(topic2);
        messageProducer.send("foo");
        messageProducer1.send("bar");
        messageProducer1.send("baz");
        messageProducer.send("qux");

        topicManager.deleteTopic(topic1);
        topicManager.deleteTopic(topic2);
        messageConsumer.stop();
        messageConsumer1.stop();
    }

    private KafkaMessageListenerContainer initContainer(String topic1){
        KafkaMessageListenerContainer<Integer, String> container = createContainer(topic1);
        container.setupMessageListener(getListener());
        //container.setBeanName("testAuto");
        container.start();

        return container;
    }

    private MessageListener getListener(){
        return new MessageListener<Integer, String>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, String> message) {
                logger.info(message.topic() + " received: " + message.value());
                latch.countDown();
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

    private KafkaTemplate<Integer, String> createTemplate(String topic) {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, String> pf =
                new DefaultKafkaProducerFactory<Integer, String>(senderProps);
        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
        template.setDefaultTopic(topic);

        return template;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
