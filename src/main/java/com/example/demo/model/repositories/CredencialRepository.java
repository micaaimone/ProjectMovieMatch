package com.example.demo.model.repositories;

import com.example.demo.model.entities.CredencialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialRepository extends JpaRepository<CredencialEntity, Long> {
}
