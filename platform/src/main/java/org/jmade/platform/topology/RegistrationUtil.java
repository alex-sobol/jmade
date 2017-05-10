package org.jmade.platform.topology;

import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.jmade.core.Agent;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

@Component
public class RegistrationUtil implements Closeable {

    private static String AGENTS_ROOT = "/agents";

    private ZkClient zkClient;
    private String currentNodeRoot;

    public RegistrationUtil() {
        String zookeeperConnect = "localhost:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        zkClient = new ZkClient(zookeeperConnect, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
        this.currentNodeRoot = AGENTS_ROOT + "/" + new HostInfo().getIp();
    }

    public void registerNode() {
        if (!zkClient.exists(currentNodeRoot)) {
            zkClient.createPersistent(currentNodeRoot, true);
        }
    }

    public void deleteNode() {
        if (zkClient.exists(currentNodeRoot)) {
            zkClient.deleteRecursive(currentNodeRoot);
        }
    }

    public void register(Agent agent) {
        zkClient.createEphemeral(currentNodeRoot + "/" + agent.getId());
    }

    public List<String> getAgentsIds() {
        List<String> ids = zkClient.getChildren(currentNodeRoot);

        return ids;
    }

    public void delete(Agent agent) {
        zkClient.delete(currentNodeRoot + "/" + agent.getId());
    }

    @Override
    public void close() throws IOException {
        zkClient.close();
    }
}
