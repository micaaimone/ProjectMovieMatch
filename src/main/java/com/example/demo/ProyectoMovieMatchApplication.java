package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoMovieMatchApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();

		System.setProperty("API_KEY",
				dotenv.get("API_KEY"));

		System.setProperty("MERCADOPAGO_ACCESS_TOKEN",
				dotenv.get("MERCADOPAGO_ACCESS_TOKEN"));

		SpringApplication.run(ProyectoMovieMatchApplication.class, args);

		String port = dotenv.get("PORT");
		System.out.println("Puerto: " + port);	}

}
