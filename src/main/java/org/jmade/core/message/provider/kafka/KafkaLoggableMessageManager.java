package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.serialize.JsonSerializer;
import org.jmade.core.message.serialize.MessageSerializer;
import org.jmade.logs.persistence.model.MessageLog;

import java.util.Date;

public class KafkaLoggableMessageManager extends KafkaMessageManager{
    public static final String MESSAGE_LOG_CHANNEL = "message-log";
    private MessageSerializer<MessageLog> messageLogMessageSerializer = new JsonSerializer();

    public KafkaLoggableMessageManager(String id) {
        super(id);
    }

    @Override
    public void send(String channelName, String data) {
        super.send(channelName, data);
        MessageLog messageLog = createLog(data, MessageLog.TYPE_SENT);
        producer.send(MESSAGE_LOG_CHANNEL, messageLogMessageSerializer.serialize(messageLog));
    }

    @Override
    public void onMessageReceived(String channelName, String data) {
        super.onMessageReceived(channelName, data);
        MessageLog messageLog = createLog(data, MessageLog.TYPE_RECEIVED);
        producer.send(MESSAGE_LOG_CHANNEL, messageLogMessageSerializer.serialize(messageLog));
    }

    private MessageLog createLog(String content, String type){
        MessageLog messageLog = new MessageLog();
        messageLog.setDate(new Date());
        messageLog.setContent(content);
        messageLog.setType(type);
        messageLog.setActorId(id);

        return messageLog;
    }
}
