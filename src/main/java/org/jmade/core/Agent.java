package org.jmade.core;

import org.jmade.core.action.Action;
import org.jmade.core.event.EventNotificationService;
import org.jmade.core.message.MessagePublisher;
import org.jmade.core.message.MessageSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Agent {
    private EventNotificationService notificationService;

    public String id;

    private MessagePublisher publisher;
    private List<MessageSubscriber> subscribers;

    public Agent() {
        this(UUID.randomUUID().toString());
    }


    // TODO: Shouldn't be public
    public Agent(String id) {
        this.id = id;
        this.notificationService = new EventNotificationService(id);
        this.publisher = new MessagePublisher(id);
        this.subscribers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void onStart() {
        notificationService.onAgentStarted();
    }

    public void onStop() {
        subscribers.forEach(MessageSubscriber::close);
        notificationService.onAgentStopped();
    }

    // TODO: Remove
    // TODO: Lets move call of this method to behaviour
    public void send(String channel, String data) {
        publisher.send(channel, data);
        notificationService.onMessageSent(data);
    }

    public void addAction(Action action) {
        action.getChannelNames().forEach(channelName -> {
            MessageSubscriber subscriber = new MessageSubscriber(channelName);
            subscriber.setMessageProcessor((message) -> {
                notificationService.onMessageReceived(message);
                action.onMessageReceived(message);
            });
            subscriber.listenToChannel(channelName);
            subscribers.add(subscriber);
        });
    }
}
