package com.example.demo.model.repositories;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.PeliculaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeliculaRepository extends JpaRepository<PeliculaEntity, Long> {
}
