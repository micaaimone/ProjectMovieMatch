package com.example.demo.model.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class EnviromentConfig {
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
