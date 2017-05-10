package org.jmade;

import org.jmade.core.event.persistence.EventLog;
import org.jmade.core.event.persistence.EventLogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CassandraTest {

    @Autowired
    private EventLogRepository repository;

    @Test
    public void testCrudRepository() {
        EventLog event = new EventLog();
        event.setId(UUID.randomUUID());
        event.setAgentId("actor1");
        event.setContent("lol");

        repository.save(event);
        List<EventLog> events = repository.getAll();
        assert events.size() == 1;
        EventLog saved = events.get(0);
        assert saved.getAgentId().equals("actor1");

        repository.deleteAll();
    }
}
