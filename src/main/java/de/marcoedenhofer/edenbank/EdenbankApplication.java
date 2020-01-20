package de.marcoedenhofer.edenbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EdenbankApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdenbankApplication.class, args);
	}
}
