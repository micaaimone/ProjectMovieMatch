package com.example.demo.model.APIS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MoviesDatabaseAPI {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}