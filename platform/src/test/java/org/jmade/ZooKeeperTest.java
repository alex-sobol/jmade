package org.jmade;

import org.jmade.core.Agent;
import org.jmade.core.AgentRegistrationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZooKeeperTest {

    @Test
    public void testCreateDeleteNodes() throws IOException {
        AgentRegistrationUtil agentRegistrationUtil = new AgentRegistrationUtil();
        agentRegistrationUtil.register(new Agent("test"));
        agentRegistrationUtil.register(new Agent("test1"));
        List<String> agents = agentRegistrationUtil.getAgentsIds();
        assert agents.size() == 2;

        agentRegistrationUtil.delete(new Agent("test"));
        agentRegistrationUtil.delete(new Agent("test1"));
        agents = agentRegistrationUtil.getAgentsIds();
        assert agents.size() == 0;

        agentRegistrationUtil.close();
    }

    @Test
    public void testWriteEphemeralNodes() throws IOException {
        AgentRegistrationUtil agentRegistrationUtil = new AgentRegistrationUtil();
        agentRegistrationUtil.register(new Agent("test2"));
        agentRegistrationUtil.register(new Agent("test3"));
        List<String> agents = agentRegistrationUtil.getAgentsIds();
        assert agents.size() == 2;
        agentRegistrationUtil.close();

        agentRegistrationUtil = new AgentRegistrationUtil();
        agents = agentRegistrationUtil.getAgentsIds();
        assert agents.size() == 0;
        agentRegistrationUtil.close();
    }

    @Test
    public void testEphemeralNodesWithoutCloseClient() throws IOException {
        AgentRegistrationUtil agentRegistrationUtil = new AgentRegistrationUtil();
        agentRegistrationUtil.register(new Agent("test4"));
        List<String> agents = agentRegistrationUtil.getAgentsIds();
        assert agents.size() == 1;
    }
}
