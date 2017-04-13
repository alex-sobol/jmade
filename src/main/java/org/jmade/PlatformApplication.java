package org.jmade;

import org.jmade.core.Agent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlatformApplication {

	public static void main(String[] args) {
		System.out.println("Well");

		Agent a1 = new Agent();
		a1.onStart();
		Agent a2 = new Agent();
		a2.onStart();

		System.err.println("a1 ID=" + a1.ID);
		System.err.println("a2 ID=" + a1.ID);

		a1.broadCastMessage("1 message");
		a1.consume();
		a2.consume();

		a1.broadCastMessage("2 message");
		a1.consume();
		a2.consume();

		a1.sendMessage(a2.ID, "1 private message from a1 to a2");

		Agent a3 = new Agent();
		a3.onStart();

		a1.broadCastMessage("3 message");
		a1.consume();
		a2.consume();
		a3.consume();

		a1.sendMessage(a2.ID, "2 message from a1 to a2");
		a1.consume();
		a2.consume();
		a3.consume();


		a2.sendMessage(a3.ID, "3 message from a2 to a3");
		a1.consume();
		a2.consume();
		a3.consume();

		a1.onStop();
		a2.onStop();
		a3.onStop();

		SpringApplication.run(PlatformApplication.class, args);
	}
}
