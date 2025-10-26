package com.example.demo.model.config;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Objects;

@Configuration
public class EnvironmentConfig {

    //cargamos las variables de entorno en el .env y las seteamos con properties
    //dsps las leemos con @Value
    static {
        try {
            System.out.println("=== INICIANDO CARGA DE .env ===");
            System.out.println("Working directory actual: " + new File(".").getAbsolutePath());

            File envFile = new File(".env");
            System.out.println("¿Existe .env en " + envFile.getAbsolutePath() + "? " + envFile.exists());

            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .load();

            System.setProperty("API_KEY",
                    Objects.requireNonNull(dotenv.get("API_KEY")));

            System.setProperty("MERCADOPAGO_ACCESS_TOKEN",
                    Objects.requireNonNull(dotenv.get("MERCADOPAGO_ACCESS_TOKEN")));

            String port = dotenv.get("PORT");
            System.out.println("Puerto: " + port);

            System.setProperty("MAIL",
                    Objects.requireNonNull(dotenv.get("MAIL")));

            System.setProperty("MAIL_PASSWORD",
                    Objects.requireNonNull(dotenv.get("MAIL_PASSWORD")));

            System.setProperty("jwt.secret",
                    Objects.requireNonNull(dotenv.get("JWT_SECRET")));

            System.out.println("✓ Variables de entorno cargadas correctamente");

        } catch (Exception e) {
            System.err.println("ERROR al cargar .env:");
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar el archivo .env", e);
        }
    }
    //esto registra el resttemplate para q lo podamos inyectar a donde necesitemos
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
