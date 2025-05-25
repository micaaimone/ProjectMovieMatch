package com.example.demo.model.repositories;

import com.example.demo.model.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    public List<UsuarioEntity> findByEdadGreaterThan(int edad);
    public UsuarioEntity findByUsername(String username);
    // public UsuarioEntity findByEmail(String email);
}
