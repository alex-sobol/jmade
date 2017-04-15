package org.jmade.core;

import org.jmade.core.message.ACLMessage;
import org.jmade.core.message.MessageManager;
import org.jmade.core.message.MessageProcessor;

import java.util.List;
import java.util.UUID;

public class Agent implements MessageProcessor{
    private static final String BROADCAST_TOPIC = "broadcast";

    public String id;

    private MessageManager messageManager;

    public Agent() {
        this(UUID.randomUUID().toString());
    }

    public Agent(String id){
        this.id = id;
    }

    public void onStart() {
        messageManager = new MessageManager(id, this);
    }

    public void onStop() {
        messageManager.stop();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMessageReceived(ACLMessage message) {
        System.err.println(message.getContent());
    }

    public void dummySend(String id, List<String> messages){
        messages.forEach(message->{
            messageManager.respond(new ACLMessage(id, message), message);
        });
    }
}
