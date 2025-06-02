package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.User.CredencialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialRepository extends JpaRepository<CredencialEntity, Long> {
}
