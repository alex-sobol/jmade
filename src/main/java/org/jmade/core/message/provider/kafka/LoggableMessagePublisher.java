package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.logs.persistence.model.MessageLog;

import java.util.Date;

public class LoggableMessagePublisher extends MessagePublisher {

    public static final String MESSAGE_LOG_CHANNEL = "message-log";
    private JsonConverter<MessageLog> messageLogMessageConverter = new JsonConverter<>(MessageLog.class);

    public LoggableMessagePublisher(String id) {
        super(id);
    }

    // TODO: What if log message sending fails. SENDING vs SENT
    @Override
    public void send(String channelName, String data) {
        super.send(channelName, data);

        MessageLog messageLog = createLog(data, MessageLog.TYPE_SENT);
        ACLMessage aclMessage = new ACLMessage(id, messageLogMessageConverter.serialize(messageLog));
        producer.send(MESSAGE_LOG_CHANNEL, converter.serialize(aclMessage));
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
