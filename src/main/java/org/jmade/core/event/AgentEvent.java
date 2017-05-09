package org.jmade.core.event;

import java.util.Date;

public class AgentEvent {
    private String type;
    private String agentId;
    private Date createdDate = new Date();
    private String content;

    public AgentEvent() {
    }

    public AgentEvent(String type, String agentId, String content) {
        this.type = type;
        this.agentId = agentId;
        this.content = content;
        this.createdDate = new Date();
    }

    public AgentEvent(String type, String agentId) {
        this.type = type;
        this.agentId = agentId;
    }

    public String getType() {
        return type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getContent() {
        return content;
    }
}
