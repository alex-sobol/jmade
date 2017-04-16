package org.jmade.core.logs.persistence.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

@Table
public class Event {
    @PrimaryKey
    private UUID id;
    private String actorId;
    private String message;

    public Event() {
    }

    public Event(UUID id, String actorId, String message) {
        this.id = id;
        this.actorId = actorId;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
