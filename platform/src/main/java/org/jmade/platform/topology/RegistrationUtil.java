package org.jmade.platform.topology;

import org.I0Itec.zkclient.ZkClient;
import org.jmade.core.Agent;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationUtil implements Closeable {

    private static String AGENTS_ROOT = "/agents";

    private ZkClient zkClient;
    private String currentNodeRoot;

    public RegistrationUtil(ZkClient zkClient) {
        this.zkClient = zkClient;
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

    public Map<String, List> getTopology() {
        Map<String, List> topology = new HashMap<>();
        List<String> nodes = zkClient.getChildren(AGENTS_ROOT);
        nodes.forEach(node -> {
            List<String> agents = zkClient.getChildren(AGENTS_ROOT + "/" + node);
            topology.put(node, agents);
        });

        return topology;
    }

    public List<String> getAgentsIds() {
        List<String> ids = zkClient.getChildren(currentNodeRoot);

        return ids;
    }

    public void delete(Agent agent) {
        zkClient.delete(currentNodeRoot + "/" + agent.getId());
    }

    public void clean() {
        if (zkClient.exists(AGENTS_ROOT)) {
            zkClient.deleteRecursive(AGENTS_ROOT);
        }
    }

    @Override
    public void close() throws IOException {
        zkClient.close();
    }
}
