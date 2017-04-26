package org.jmade;

import org.jmade.core.Agent;
import org.jmade.example.Buyer;
import org.jmade.example.Seller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PlatformApplication {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Well");

		SpringApplication.run(PlatformApplication.class, args);

		List<Agent> agents = new ArrayList<>();
		Buyer buyer = new Buyer("buyer1",1000.0, 0.1);
		Buyer buyer1 = new Buyer("buyer2",1000.0, 1.0);
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
