package org.jmade.core;

import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class AgentRegistrationUtil {

    private static String AGENTS_ROOT = "/agents";

    private ZkClient zkClient;

    public AgentRegistrationUtil() {
        String zookeeperConnect = "localhost:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        // TODO: Add client close
        zkClient = new ZkClient(zookeeperConnect, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
    }

    public void register(Agent agent){
        zkClient.createPersistent(AGENTS_ROOT + "/" + agent.getId(), true);
    }

    public List<String> getAgents(){
        List<String> ids = zkClient.getChildren(AGENTS_ROOT);

        return ids;
    }

    public void delete(Agent agent){
        zkClient.delete(AGENTS_ROOT + "/" + agent.getId());
    }

    // TODO: Consider using ephimeral nodes in ZooKeeper
    public void clean(){
        zkClient.deleteRecursive(AGENTS_ROOT);
    }
}
