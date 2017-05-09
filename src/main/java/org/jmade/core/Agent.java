package org.jmade.core;

import org.jmade.core.event.EventNotificationService;
import org.jmade.core.message.ACMessage;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessagePublisher;
import org.jmade.core.message.MessageSubscriber;
import org.jmade.core.message.serialize.JsonConverter;

import java.util.List;
import java.util.UUID;

//todo: move MessageProcessor implementation to kinda "Behaviour" class
public class Agent implements MessageProcessor {
    private EventNotificationService notificationService;

    public String id;

    protected MessagePublisher publisher;
    protected MessageSubscriber subscriber;

    public Agent() {
        this(UUID.randomUUID().toString());
    }

    // TODO: Shouldn't be public
    public Agent(String id) {
        this.id = id;
        this.notificationService = new EventNotificationService(id);
    }

    public void onStart() {
        this.publisher = new MessagePublisher(id);
        this.subscriber = new MessageSubscriber(id);
        this.subscriber.setMessageProcessor(this);
        notificationService.onAgentStarted();
    }

    public void onStop() {
        subscriber.close();
        notificationService.onAgentStopped();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMessageReceived(ACMessage message) {
        notificationService.onMessageReceived(new JsonConverter<>(ACMessage.class).serialize(message));
    }

    public void dummySend(String id, List<String> messages) {
        messages.forEach(message -> {
            send(id, message);
        });
    }

   /* private Producer p;
    private List<Consumer> cons;*/

    // TODO: Lets move call of this method to behaviour
    protected void reply(ACMessage message, String content) {
        send(message.getSenderId(), content);
    }

    // TODO: Remove
    protected void send(String channel, String data) {
        publisher.send(channel, data);
        notificationService.onMessageSent(data);
    }



   /* protected abstract <T extends ACLMessage> Class<T> getDataType();*/

}
