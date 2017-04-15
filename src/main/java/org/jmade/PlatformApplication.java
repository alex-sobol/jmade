package org.jmade;

import org.jmade.core.Agent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlatformApplication {

	public static void main(String[] args) {
		System.out.println("Well");

		SpringApplication.run(PlatformApplication.class, args);
	}
}
