package org.jmade;

import org.jmade.core.AgentRunner;
import org.jmade.core.event.persistence.EventLogRepository;
import org.jmade.core.event.persistence.EventLogger;
import org.jmade.example.Buyer;
import org.jmade.example.Seller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class PlatformApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlatformApplication.class);
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(PlatformApplication.class, args);
        EventLogRepository eventLogRepository = context.getBean(EventLogRepository.class);
        AgentRunner agentRunner = new AgentRunner();
        //agentRunner.run(new MessagesLogger(messageLogRepository));
        new EventLogger(eventLogRepository);
        agentRunner.run(new Buyer("buyer1", 1000.0, 2.0, 0.1));
        agentRunner.run(new Buyer("buyer2", 1000.0, 1.0, 1.0));
        agentRunner.run(new Seller("seller"));
    }
}
