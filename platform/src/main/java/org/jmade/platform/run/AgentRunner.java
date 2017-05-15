package org.jmade.platform.run;

import org.jmade.core.Agent;
import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AgentRunner implements Closeable {

    private final List<Agent> agents;
    private ExecutorService pool;
    private RegistrationUtil registrationUtil;

    public AgentRunner(RegistrationUtil registrationUtil) {
        this.agents = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(10);
        this.registrationUtil = registrationUtil;
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
