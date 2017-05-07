package org.jmade.core.message.provider.kafka;


import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.logs.persistence.model.MessageLog;

import java.util.Date;

public class LoggableMessageSubscriber extends MessageSubscriber {

    public static final String MESSAGE_LOG_CHANNEL = "message-log";
    private JsonConverter<MessageLog> messageLogMessageConverter = new JsonConverter<>(MessageLog.class);
    private MessagePublisher publisher;
    private MessageConverter converter;

    public LoggableMessageSubscriber(String id) {
        super(id);
        this.publisher = new MessagePublisher(id);
        this.converter = new JsonConverter<>(Object.class);
    }

    // TODO: IMQ or behaviour in case of message processing errors ????
    @Override
    public void onMessageReceived(String channelName, String data) {
        super.onMessageReceived(channelName, data);
        MessageLog messageLog = createLog(data, MessageLog.TYPE_RECEIVED);
        ACLMessage aclMessage = new ACLMessage(id, messageLogMessageConverter.serialize(messageLog));
        publisher.send(MESSAGE_LOG_CHANNEL, converter.serialize(aclMessage));
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
