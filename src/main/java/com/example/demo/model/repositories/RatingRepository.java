package com.example.demo.model.repositories;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
}
