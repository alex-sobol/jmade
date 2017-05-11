package org.jmade.core.event.persistence;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventLogRepository extends CrudRepository<EventLog, UUID> {
    @Query("select * from event_log")
    public List<EventLog> getAll();

    @Query("select * from event_log where agentId=:agentId and createdDate>:date")
    public List<EventLog> findAllByAgentAndDateLaterThen(@Param("agentId") String agentId, @Param("date") Date date);

    @Query("select * from event_log where agentId=:agentId")
    public List<EventLog> findAllByAgent(@Param("agentId") String agentId);
}
