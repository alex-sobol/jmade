package org.jmade.core.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    private final String name;
    private final KafkaConsumer<String, String> consumer;

    public Consumer(String name, Properties properties) {
        properties.put("group.id", "consumers");//name);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(name));
        this.name = name;
    }

    public void consume() {
        System.out.println("Starting consumption: " + name);
       /* while(true) {*/
            final ConsumerRecords<String, String> records = consumer.poll(100);
            //System.out.println("Polling for " + name + " found " + records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.err.println("Received " + name + ": " + record);
            }
      /*  }*/

    }

    public void close(){
        consumer.close();
    }
}
