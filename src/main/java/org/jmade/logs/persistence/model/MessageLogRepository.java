package org.jmade.logs.persistence.model;

import org.jmade.logs.persistence.model.Event;
import org.jmade.logs.persistence.model.MessageLog;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MessageLogRepository extends CrudRepository<MessageLog, UUID> {
    @Query("select * from message_log")
    public List<Event> getAll();
}
