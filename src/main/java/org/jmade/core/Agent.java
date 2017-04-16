package org.jmade.core;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.provider.kafka.KafkaMessageManager;
import org.jmade.core.message.MessageProcessor;

import java.util.List;
import java.util.UUID;

public class Agent implements MessageProcessor{
    private static final String BROADCAST_TOPIC = "broadcast";

    public String id;

    private KafkaMessageManager kafkaMessageManager;

    public Agent() {
        this(UUID.randomUUID().toString());
    }

    public Agent(String id){
        this.id = id;
    }

    public void onStart() {
        kafkaMessageManager = new KafkaMessageManager(id, this);
    }

    public void onStop() {
        kafkaMessageManager.stop();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMessageReceived(ACLMessage message) {
        System.err.println(message.getContent());
    }

    public void dummySend(String id, List<String> messages){
        messages.forEach(message->{
            kafkaMessageManager.respond(new ACLMessage(id, message), message);
        });
    }
}
