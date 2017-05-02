package org.jmade.core.message.provider.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.jmade.core.message.MessageProducer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaMessageProducer implements MessageProducer {

    private KafkaTemplate<Integer, String> template;

    public KafkaMessageProducer() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
        template = new KafkaTemplate<>(pf);
    }

    @Override
    public void send(String topic, String data) {
        template.send(topic, data);
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
