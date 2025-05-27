package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
}
