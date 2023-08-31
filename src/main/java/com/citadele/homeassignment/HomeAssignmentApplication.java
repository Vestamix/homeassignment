package com.citadele.homeassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.citadele.homeassignment")
@EntityScan(basePackages = "com.citadele.homeassignment.repository.entity")
@EnableJpaRepositories(basePackages = "com.citadele.homeassignment.repository")
public class HomeAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAssignmentApplication.class, args);
	}

}
