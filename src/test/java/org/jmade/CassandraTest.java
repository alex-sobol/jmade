package org.jmade;

import org.jmade.logs.persistence.model.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.jmade.logs.persistence.model.EventRepository;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CassandraTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testCrudRepository() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setActorId("actor1");
        event.setMessage("lol");

        eventRepository.save(event);
        List<Event> events = eventRepository.getAll();
        assert events.size() == 1;
        Event saved = events.get(0);
        assert saved.getActorId().equals("actor1");

        eventRepository.deleteAll();

    }

}
