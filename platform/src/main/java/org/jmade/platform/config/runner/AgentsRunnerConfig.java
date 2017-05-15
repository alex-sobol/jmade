package org.jmade.platform.config.runner;

import org.jmade.platform.run.AgentRunner;
import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentsRunnerConfig {

    @Autowired
    RegistrationUtil registrationUtil;

    @Value("${jmade.agent.runner.nThreads}")
    Integer nThreads;

    @Bean
    public AgentRunner agentRunner(){
        return new AgentRunner(registrationUtil, nThreads);
    }
}
