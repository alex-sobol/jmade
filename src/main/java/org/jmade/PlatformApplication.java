package org.jmade;

import org.jmade.core.AgentRunner;
import org.jmade.example.Buyer;
import org.jmade.example.Seller;
import org.jmade.logs.persistence.MessagesLogger;
import org.jmade.logs.persistence.model.MessageLogRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class PlatformApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(PlatformApplication.class, args);
        MessageLogRepository messageLogRepository = context.getBean(MessageLogRepository.class);
        AgentRunner agentRunner = new AgentRunner();
        //agentRunner.run(new MessagesLogger(messageLogRepository));
        new MessagesLogger(messageLogRepository).onStart();
        agentRunner.run(new Buyer("buyer1", 1000.0, 2.0, 0.1));
        agentRunner.run(new Buyer("buyer2", 1000.0, 1.0, 1.0));
        agentRunner.run(new Seller("seller"));
    }
}
