package org.jmade.core.message.provider.kafka;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    protected String id;
    protected KafkaMessageProducer producer;
    protected MessageConverter<ACLMessage> converter;

    public MessagePublisher(String id) {
        this.id = id;
        producer = new KafkaMessageProducer();
        converter = new JsonConverter<>(ACLMessage.class);
    }

    public void send(String channelName, String data) {
        ACLMessage aclMessage = new ACLMessage(id, data);


        producer.send(channelName, converter.serialize(aclMessage));
    }

    //todo:add enable logs.
    // Send and receive messsages - different case. MessageLog(type(sent-received), actorId, time, message)
}
