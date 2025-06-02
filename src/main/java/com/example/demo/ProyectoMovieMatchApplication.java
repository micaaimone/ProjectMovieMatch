package com.example.demo;

import com.example.demo.model.entities.CredencialEntity;
import com.example.demo.model.enums.E_Cargo;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProyectoMovieMatchApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProyectoMovieMatchApplication.class, args);

	}

}
