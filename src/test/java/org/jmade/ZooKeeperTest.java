package org.jmade;

import org.jmade.core.Agent;
import org.jmade.core.AgentRegistrationUtil;
import org.jmade.logs.persistence.model.Event;
import org.jmade.logs.persistence.model.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZooKeeperTest {

    @Test
    public void testWriteToZooKeeper() {
        AgentRegistrationUtil agentRegistrationUtil = new AgentRegistrationUtil();
        agentRegistrationUtil.register(new Agent("test"));
        agentRegistrationUtil.register(new Agent("test1"));
        List<String> agents = agentRegistrationUtil.getAgents();
        assert agents.size() == 2;

        agentRegistrationUtil.delete(new Agent("test"));
        agentRegistrationUtil.delete(new Agent("test1"));

        agents = agentRegistrationUtil.getAgents();
        assert agents.size() == 0;
    }

}
