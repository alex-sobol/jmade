package org.jmade.web.controller.rest;

import org.jmade.core.event.persistence.EventLog;
import org.jmade.core.event.persistence.EventLogRepository;
import org.jmade.core.message.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgentRestController {

    private static final String UI_PUBLISHER = "ui_user";

    @Autowired
    EventLogRepository repository;

    @RequestMapping("/agent/logs")
    public List<EventLog> logs(@RequestParam("agentId") String agentId, @RequestParam("date") String dateString) {
        List<EventLog> result;
        if (dateString != "") {
            Date date = new Date(Long.parseLong(dateString));
            result = repository.findAllByAgentAndDateLaterThen(agentId, date);
        } else {
            result = repository.findAllByAgent(agentId);
        }

        return result;
    }

    @RequestMapping(value = "/agent/send", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void send(@RequestParam("agentId") String agentId, @RequestParam("message") String message) {
        MessagePublisher publisher = new MessagePublisher(UI_PUBLISHER);
        publisher.send(agentId, message);
    }
}
