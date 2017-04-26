package org.jmade.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.provider.kafka.KafkaMessageManager;
import org.jmade.core.message.serialize.JsonSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.jmade.logs.persistence.model.Event;
import org.jmade.logs.persistence.model.EventLevel;

public class EventSendingLogger {

    private static final String LOGS_CHANNEL = "logs";

    private MessageManager messageManager;
    //TODO: clean object mapper;
    private MessageSerializer serializer;

    public EventSendingLogger() {
        //TODO: listen to channel not always required. Separate
        this.messageManager = new KafkaMessageManager(LOGS_CHANNEL, null);
        this.serializer = new JsonSerializer();
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
        messageManager.send(serializer.serialize(event));
    }

}
