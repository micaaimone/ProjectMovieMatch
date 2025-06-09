package com.example.demo.Seguridad.repositories;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    Optional<CredentialsEntity> findByEmail(String email);
}
