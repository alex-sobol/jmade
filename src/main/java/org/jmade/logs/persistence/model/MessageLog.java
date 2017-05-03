package org.jmade.logs.persistence.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table("message_log")
public class MessageLog {
    public static final String TYPE_SENT = "SENT";
    public static final String TYPE_RECEIVED = "RECEIVED";

    @PrimaryKey
    private UUID id;
    private String actorId;
    private String type;
    private Date createdDate;
    private String content;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
}
