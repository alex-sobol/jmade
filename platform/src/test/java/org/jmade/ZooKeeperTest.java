package org.jmade;

import org.jmade.core.Agent;
import org.jmade.platform.topology.RegistrationUtil;
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
        RegistrationUtil registrationUtil = new RegistrationUtil();
        registrationUtil.register(new Agent("test"));
        registrationUtil.register(new Agent("test1"));
        List<String> agents = registrationUtil.getAgentsIds();
        assert agents.size() == 2;

        registrationUtil.delete(new Agent("test"));
        registrationUtil.delete(new Agent("test1"));
        agents = registrationUtil.getAgentsIds();
        assert agents.size() == 0;

        registrationUtil.close();
    }

    @Test
    public void testWriteEphemeralNodes() throws IOException {
        RegistrationUtil registrationUtil = new RegistrationUtil();
        registrationUtil.register(new Agent("test2"));
        registrationUtil.register(new Agent("test3"));
        List<String> agents = registrationUtil.getAgentsIds();
        assert agents.size() == 2;
        registrationUtil.close();

        registrationUtil = new RegistrationUtil();
        agents = registrationUtil.getAgentsIds();
        assert agents.size() == 0;
        registrationUtil.close();
    }

    @Test
    public void testEphemeralNodesWithoutCloseClient() throws IOException {
        RegistrationUtil registrationUtil = new RegistrationUtil();
        registrationUtil.register(new Agent("test4"));
        List<String> agents = registrationUtil.getAgentsIds();
        assert agents.size() == 1;
    }
}
