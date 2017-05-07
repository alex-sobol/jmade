package org.jmade.core;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.provider.kafka.MessagePublisher;
import org.jmade.core.message.provider.kafka.MessageSubscriber;
import org.jmade.logs.EventSendingLogger;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//todo: move MessageProcessor implementation to kinda "Behaviour" class
public class Agent implements MessageProcessor {
    protected EventSendingLogger eventSendingLogger = new EventSendingLogger();

    public String id;

    protected MessagePublisher publisher;
    protected MessageSubscriber subscriber;

    public Agent() {
        this(UUID.randomUUID().toString());
    }

    // TODO: Shouldn't be public
    public Agent(String id) {
        this.id = id;
    }

    public void onStart() {
        this.publisher = new MessagePublisher(id);
        this.subscriber = new MessageSubscriber(id);
        this.subscriber.setMessageProcessor(this);
    }

    public void onStop() {
        subscriber.close();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
    }

    public void dummySend(String id, List<String> messages) {
        messages.forEach(message -> {
            publisher.send(id, message);
            eventSendingLogger.message(message);
        });
    }

   /* private Producer p;
    private List<Consumer> cons;*/

    // TODO: Lets move call of this method to behaviour
    protected void reply(ACLMessage message, String content) {
        publisher.send(message.getSenderId(), content);
        eventSendingLogger.message(content);
    }

    // TODO: Remove
    protected void send(String channel, String data) {
        publisher.send(channel, data);
    }

   /* protected abstract <T extends ACLMessage> Class<T> getDataType();*/
}
