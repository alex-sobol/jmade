package org.jmade.core.event;

import org.jmade.core.message.provider.kafka.MessagePublisher;
import org.jmade.core.message.serialize.MessageConverter;

public class EventNotificationService {

    public static final String EVENT_LOG_CHANNEL = "agent-events";

    private String agentId;

    private MessagePublisher publisher;
    private MessageConverter<AgentEvent> converter;

    public EventNotificationService(String agentId) {
        this.agentId = agentId;
        this.publisher = new MessagePublisher(agentId);
    }

    public void onAgentStarted(){
        AgentEvent event = new AgentEvent(EventType.AGENT_STARTED, agentId, "");
       send(event);
    }


    public void onAgentStopped(){
        AgentEvent event = new AgentEvent(EventType.AGENT_STOPPED, agentId, "");
        send(event);
    }

    public void onMessageReceived(String message){
        AgentEvent event = new AgentEvent(EventType.MESSAGE_RECEIVED, agentId, message);
        send(event);
    };

    public void onMessageSent(String message){
        AgentEvent event = new AgentEvent(EventType.MESSAGE_SENT, agentId, message);
        send(event);
    }

    private void send(AgentEvent event){
        publisher.send(EVENT_LOG_CHANNEL, converter.serialize(event));
    }
}
