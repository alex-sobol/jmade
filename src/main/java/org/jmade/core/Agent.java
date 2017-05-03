package org.jmade.core;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.provider.kafka.KafkaLoggableMessageManager;
import org.jmade.core.message.provider.kafka.KafkaMessageManager;
import org.jmade.core.message.MessageProcessor;
import org.jmade.logs.EventSendingLogger;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Agent implements MessageProcessor {
    protected EventSendingLogger eventSendingLogger = new EventSendingLogger();

    public String id;

    protected KafkaMessageManager kafkaMessageManager;

    public Agent() {
        this(UUID.randomUUID().toString());
    }

    public Agent(String id) {
        this.id = id;
    }

    public void onStart() {
        kafkaMessageManager = new KafkaLoggableMessageManager(id);
        kafkaMessageManager.setMessageProcessor(this);
    }

    public void onStop() {
        kafkaMessageManager.stop();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
        System.err.println(message.getContent());
    }

    public void dummySend(String id, List<String> messages) {
        messages.forEach(message -> {
            kafkaMessageManager.send(id, message);
            eventSendingLogger.message(message);
        });
    }

    protected void reply(ACLMessage message, String content) {
        kafkaMessageManager.respond(message, content);
        eventSendingLogger.message(content);
    }

    protected void send(String channel, String data){
        kafkaMessageManager.send(channel, data);
    }
}
