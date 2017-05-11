package org.jmade.web.controller.rest;

import org.jmade.core.event.persistence.EventLog;
import org.jmade.core.event.persistence.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgentRestController {

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
}
