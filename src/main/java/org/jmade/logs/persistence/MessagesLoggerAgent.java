package org.jmade.logs.persistence;

import org.jmade.core.Agent;
import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.provider.kafka.KafkaLoggableMessageManager;
import org.jmade.core.message.provider.kafka.KafkaMessageManager;
import org.jmade.core.message.serialize.MessageLogSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.jmade.logs.persistence.model.MessageLog;
import org.jmade.logs.persistence.model.MessageLogRepository;

import java.io.IOException;
import java.util.UUID;

public class MessagesLoggerAgent extends Agent {

    private static final String LOGGER_GROUP = "LOGGERS";

    private MessageSerializer<MessageLog> messageLogMessageSerializer = new MessageLogSerializer();
    private MessageLogRepository messageLogRepository;

    public MessagesLoggerAgent(MessageLogRepository messageLogRepository) {
        super(UUID.randomUUID().toString());
        this.messageLogRepository = messageLogRepository;
    }

    @Override
    public void onStart() {
        kafkaMessageManager = new KafkaMessageManager(id);
        kafkaMessageManager.setMessageProcessor(this);
        kafkaMessageManager.listenToChannel(KafkaLoggableMessageManager.MESSAGE_LOG_CHANNEL, LOGGER_GROUP);
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
        super.onMessageReceived(message);
        MessageLog messageLog = messageLogMessageSerializer.deserialize(message.getContent());
        messageLog.setId(UUID.randomUUID());
        messageLogRepository.save(messageLog);
    }
}
