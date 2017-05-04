package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.logs.persistence.model.MessageLog;

import java.util.Date;

public class KafkaLoggableMessageManager extends KafkaMessageManager {
    public static final String MESSAGE_LOG_CHANNEL = "message-log";
    private JsonConverter<MessageLog> messageLogMessageConverter = new JsonConverter<>(MessageLog.class);

    public KafkaLoggableMessageManager(String id) {
        super(id);
    }

    //todo: send object
    @Override
    public void send(String channelName, String data) {
        // TODO: What if log message sending fails. SENDING vs SENT
        super.send(channelName, data);

        MessageLog messageLog = createLog(data, MessageLog.TYPE_SENT);
        ACLMessage aclMessage = new ACLMessage(id, messageLogMessageConverter.serialize(messageLog));
        producer.send(MESSAGE_LOG_CHANNEL, messageConverter.serialize(aclMessage));
    }

    // TODO: IMQ or behaviour in case of message processing errors ????
    @Override
    public void onMessageReceived(String channelName, String data) {
        super.onMessageReceived(channelName, data);
        MessageLog messageLog = createLog(data, MessageLog.TYPE_RECEIVED);
        ACLMessage aclMessage = new ACLMessage(id, messageLogMessageConverter.serialize(messageLog));
        producer.send(MESSAGE_LOG_CHANNEL, messageConverter.serialize(aclMessage));
    }

    private MessageLog createLog(String content, String type) {
        MessageLog messageLog = new MessageLog();
        messageLog.setCreatedDate(new Date());
        messageLog.setContent(content);
        messageLog.setType(type);
        messageLog.setActorId(id);

        return messageLog;
    }
}
