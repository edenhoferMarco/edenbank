package de.marcoedenhofer.edenbank;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EdenbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdenbankApplication.class, args);
	}

	/* Configuration for thymeleaf to use the LayoutDialect */
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}


}
