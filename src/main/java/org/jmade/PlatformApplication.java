package org.jmade;

import org.jmade.core.Agent;
import org.jmade.example.Buyer;
import org.jmade.example.Seller;
import org.jmade.logs.persistence.MessagesLoggerAgent;
import org.jmade.logs.persistence.model.MessageLogRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class PlatformApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Well");

        ConfigurableApplicationContext context = SpringApplication.run(PlatformApplication.class, args);
        MessageLogRepository messageLogRepository = context.getBean(MessageLogRepository.class);
        List<Agent> agents = new ArrayList<>();
        MessagesLoggerAgent messagesLoggerAgent = new MessagesLoggerAgent(messageLogRepository);
        messagesLoggerAgent.onStart();
        Buyer buyer = new Buyer("buyer1", 1000.0, 0.1);
        Buyer buyer1 = new Buyer("buyer2", 1000.0, 1.0);
        Seller seller = new Seller("seller");
        buyer.onStart();
        buyer1.onStart();
        seller.onStart();

		/*agents.add(buyer);
        agents.add(buyer1);
		agents.add(seller);
		final ExecutorService pool = Executors.newFixedThreadPool(10);

		agents.forEach(agent->{
			pool.execute(() -> {
				agent.onStart();
			});

		});

		agents.forEach(agent->{
			agent.onStop();
		});

		pool.shutdown();
		while (!pool.isShutdown()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(PlatformApplication.class.getName()).log(Level.SEVERE, null, ex);
			}
		}*/
        SpringApplication.run(PlatformApplication.class, args);
    }
}
