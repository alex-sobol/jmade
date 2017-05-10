package org.jmade.core;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgentRunner implements Closeable {

    private final List<Agent> agents;
    private ExecutorService pool;
    private AgentRegistrationUtil registrationUtil = new AgentRegistrationUtil();

    public AgentRunner() {
        this.agents = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(10);
    }

    public void run(Agent agent) {
        agents.add(agent);
        pool.execute(() -> {
            agent.onStart();
            registrationUtil.register(agent);
        });
    }

    @Override
    public void close() {
        agents.forEach(Agent::onStop);
        agents.forEach(registrationUtil::delete);
    }
}