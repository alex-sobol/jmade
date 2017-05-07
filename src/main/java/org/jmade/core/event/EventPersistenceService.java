package org.jmade.core.event;


import org.jmade.core.message.provider.kafka.MessageSubscriber;

import java.util.UUID;

public class EventPersistenceService {

    private static final String AGENT_EVENT_LOGGERS_GROUP = "loggers";

    private MessageSubscriber subscriber;

    public EventPersistenceService() {
        this.subscriber = new MessageSubscriber();
        this.subscriber.listenToChannel(EventNotificationService.EVENT_LOG_CHANNEL, AGENT_EVENT_LOGGERS_GROUP);
    }
}
