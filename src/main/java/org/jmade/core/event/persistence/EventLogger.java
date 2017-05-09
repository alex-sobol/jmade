package org.jmade.core.event.persistence;


import org.jmade.core.event.EventNotificationService;
import org.jmade.core.message.ACMessage;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessageSubscriber;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

public class EventLogger implements MessageProcessor, Closeable {

    private static final String AGENT_EVENT_LOGGERS_GROUP = "loggers";

    private EventLogRepository eventLogRepository;
    protected MessageSubscriber subscriber;
    private MessageConverter<EventLog> converter;

    public EventLogger(EventLogRepository eventLogRepository) {
        this.eventLogRepository = eventLogRepository;
        this.subscriber = new MessageSubscriber(AGENT_EVENT_LOGGERS_GROUP);
        this.subscriber.setMessageProcessor(this);
        this.subscriber.listenToChannel(EventNotificationService.EVENT_LOG_CHANNEL, AGENT_EVENT_LOGGERS_GROUP);
        this.converter = new JsonConverter<>(EventLog.class);
    }


    @Override
    public void onMessageReceived(ACMessage message) {
        EventLog eventLog = converter.deserialize(message.getContent());
        eventLog.setId(UUID.randomUUID());
        eventLogRepository.save(eventLog);
    }

    @Override
    public void close() throws IOException {
        subscriber.close();
    }
}
