package org.jmade.core;

import org.jmade.core.message.Stoppable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgentRunner implements Stoppable {

    private final List<Agent> agents;
    private ExecutorService pool;

    public AgentRunner() {
        this.agents = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(10);
    }

    public void run(Agent agent) {
        agents.add(agent);
        pool.execute(() -> {
            agent.onStart();
        });
    }

    @Override
    public void stop() {
        agents.forEach(Agent::onStop);
    }
}
