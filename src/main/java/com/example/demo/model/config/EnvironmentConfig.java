package com.example.demo.model.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EnvironmentConfig {

    //cargamos las variables de entorno en el .env y las seteamos con properties
    //dsps las leemos con @Value
    static {
        Dotenv dotenv = Dotenv.configure().load();

        System.setProperty("API_KEY",
                dotenv.get("API_KEY"));

        System.setProperty("MERCADOPAGO_ACCESS_TOKEN",
                dotenv.get("MERCADOPAGO_ACCESS_TOKEN"));

        String port = dotenv.get("PORT");
        System.out.println("Puerto: " + port);

    }

    //deberia de crear una clase nueva para mantener solid??? preguntar
    //esto registra el resttemplate para q lo podamos inyectar a donde necesitemos
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
