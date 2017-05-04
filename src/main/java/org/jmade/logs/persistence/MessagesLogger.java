package org.jmade.logs.persistence;

import org.jmade.core.Agent;
import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageProcessor;
import org.jmade.core.message.provider.kafka.KafkaLoggableMessageManager;
import org.jmade.core.message.provider.kafka.KafkaMessageManager;
import org.jmade.core.message.serialize.MessageLogSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.jmade.logs.persistence.model.MessageLog;
import org.jmade.logs.persistence.model.MessageLogRepository;

import java.io.IOException;
import java.util.UUID;

public class MessagesLogger implements MessageProcessor {

    private static final String LOGGER_GROUP = "LOGGERS";

    private MessageSerializer<MessageLog> messageLogMessageSerializer = new MessageLogSerializer();
    private MessageLogRepository messageLogRepository;
    protected KafkaMessageManager kafkaMessageManager;

    public MessagesLogger(MessageLogRepository messageLogRepository) {
        this.messageLogRepository = messageLogRepository;
    }

    public void onStart() {
        kafkaMessageManager = new KafkaMessageManager(UUID.randomUUID().toString());
        kafkaMessageManager.setMessageProcessor(this);
        kafkaMessageManager.listenToChannel(KafkaLoggableMessageManager.MESSAGE_LOG_CHANNEL, LOGGER_GROUP);
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
        MessageLog messageLog = messageLogMessageSerializer.deserialize(message.getContent());
        messageLog.setId(UUID.randomUUID());
        messageLogRepository.save(messageLog);
    }
}
