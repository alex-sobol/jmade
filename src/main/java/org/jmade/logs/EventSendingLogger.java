package org.jmade.logs;

import org.jmade.core.message.provider.kafka.MessagePublisher;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.logs.persistence.model.Event;
import org.jmade.logs.persistence.model.EventLevel;

public class EventSendingLogger {

    private static final String LOGS_CHANNEL = "logs";

    private MessagePublisher publisher;
    private MessageConverter<Event> serializer;

    public EventSendingLogger() {
        this.publisher = new MessagePublisher(LOGS_CHANNEL);
        this.serializer = new JsonConverter<>(Event.class);
    }

    public void error(String message) {
        send(EventLevel.ERROR, message);
    }

    public void debug(String message) {
        send(EventLevel.DEBUG, message);
    }

    public void message(String message) {
        send(EventLevel.MASSAGE, message);
    }

    private void send(EventLevel level, String message) {
        Event event = new Event();
        event.setEventLevel(level);
        event.setMessage(message);
        publisher.send(LOGS_CHANNEL, serializer.serialize(event));
    }
}
