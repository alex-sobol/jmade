package org.jmade.logs.persistence;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.MessageSubscriber;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.logs.persistence.model.MessageLog;
import org.jmade.logs.persistence.model.MessageLogRepository;

import java.io.IOException;
import java.util.UUID;

public class MessagesLogger implements MessageProcessor {

    public static final String MESSAGE_LOG_CHANNEL = "message-log";
    private static final String LOGGER_GROUP = "LOGGERS";

    private MessageConverter<MessageLog> messageLogMessageConverter = new JsonConverter<>(MessageLog.class);
    private MessageLogRepository messageLogRepository;
    protected MessageSubscriber subscriber;

    public MessagesLogger(MessageLogRepository messageLogRepository) {
        this.messageLogRepository = messageLogRepository;
    }

    public void onStart() {
        subscriber = new MessageSubscriber(UUID.randomUUID().toString());
        subscriber.setMessageProcessor(this);
        subscriber.listenToChannel(MESSAGE_LOG_CHANNEL, LOGGER_GROUP);
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
       /* MessageLog messageLog = messageLogMessageConverter.deserialize(message.getContent());
        messageLog.setId(UUID.randomUUID());
        messageLogRepository.save(messageLog);*/
    }
}
