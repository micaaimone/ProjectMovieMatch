package com.example.demo.model.configuration;

import com.example.demo.model.entities.CredencialEntity;
import com.example.demo.model.enums.E_Cargo;
import com.example.demo.model.repositories.CredencialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initCredenciales(CredencialRepository repo) {
        return args -> {
            if (!repo.existsById(1L)) {
                repo.save(new CredencialEntity(1L, E_Cargo.ADMIN));
            }
            if (!repo.existsById(2L)) {
                repo.save(new CredencialEntity(2L, E_Cargo.USUARIO_PREMIUM));
            }
            if (!repo.existsById(3L)) {
                repo.save(new CredencialEntity(3L, E_Cargo.USUARIO));
            }
        };
    }
}
