package org.jmade.core;

import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class AgentRegistrationUtil implements Closeable {

    private static String AGENTS_ROOT = "/agents";

    //TODO: define a way to set individual namespace for every machine
    private static String CURRENT_MACHINE_ROOT = "/local";

    private ZkClient zkClient;

    public AgentRegistrationUtil() {
        String zookeeperConnect = "localhost:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        zkClient = new ZkClient(zookeeperConnect, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
        initRoot();
    }

    public void register(Agent agent) {
        zkClient.createEphemeral(AGENTS_ROOT + CURRENT_MACHINE_ROOT + "/" + agent.getId());
    }

    public List<String> getAgentsIds() {
        List<String> ids = zkClient.getChildren(AGENTS_ROOT + CURRENT_MACHINE_ROOT);

        return ids;
    }

    public void delete(Agent agent) {
        zkClient.delete(AGENTS_ROOT + CURRENT_MACHINE_ROOT + "/" + agent.getId());
    }

    @Override
    public void close() throws IOException {
        zkClient.close();
    }

    private void initRoot() {
        if (!zkClient.exists(AGENTS_ROOT + CURRENT_MACHINE_ROOT)) {
            zkClient.createPersistent(AGENTS_ROOT + CURRENT_MACHINE_ROOT, true);
        }
    }
}
