package com.example.demo.model.repositories;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.SerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<SerieEntity, Long> {
}
