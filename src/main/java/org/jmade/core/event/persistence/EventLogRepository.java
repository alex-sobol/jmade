package org.jmade.core.event.persistence;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventLogRepository extends CrudRepository<EventLog, UUID> {
    @Query("select * from message_log")
    public List<EventLog> getAll();
}
