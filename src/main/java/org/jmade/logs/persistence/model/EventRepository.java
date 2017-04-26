package org.jmade.logs.persistence.model;

import org.jmade.logs.persistence.model.Event;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends CrudRepository<Event, UUID> {
    @Query("select * from event")
    public List<Event> getAll();
}
