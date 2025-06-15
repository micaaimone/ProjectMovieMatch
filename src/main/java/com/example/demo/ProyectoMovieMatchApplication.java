package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProyectoMovieMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoMovieMatchApplication.class, args);

		//desactivar cors y csrf de security
	}

}
