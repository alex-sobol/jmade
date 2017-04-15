package org.jmade.core.message;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer extends KafkaProducer<String, String> {
    private final String topicName;

    public Producer(String topicName, Properties properties) {
        super(properties);
        this.topicName = topicName;
    }

    public void sendMessage(String message) {
        sendMessage(topicName, message);
    }

    public void sendMessage(String topicName, String message) {
        System.out.println("Sending message: " + topicName);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, "sent", "" + message + System.currentTimeMillis());
        send(producerRecord);
        System.err.println("ACLMessage sent: " + topicName);
    }


}