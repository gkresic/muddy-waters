package com.steatoda.muddywaters.megalodon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class MegalodonApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(MegalodonApplication.class, args);
		
	}

}
