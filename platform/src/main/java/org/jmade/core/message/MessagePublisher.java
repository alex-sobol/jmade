package org.jmade.core.message;

import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.core.message.transport.Producer;
import org.jmade.core.message.transport.provider.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    protected String id;
    protected Producer producer;
    protected MessageConverter<ACMessage> converter;

    public MessagePublisher(String id) {
        this.id = id;
        producer = new KafkaProducer();
        converter = new JsonConverter<>(ACMessage.class);
    }

    public void send(String channelName, String data) {
        ACMessage ACMessage = new ACMessage(id, data);

        producer.send(channelName, converter.serialize(ACMessage));
    }
}
