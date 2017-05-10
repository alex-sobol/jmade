package org.jmade.core.event.persistence;

import org.jmade.core.event.AgentEvent;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table("event_log")
public class EventLog extends AgentEvent {

    @PrimaryKey
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
