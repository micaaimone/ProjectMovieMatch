package com.example.demo.model.repositories.Subs;

import com.example.demo.model.entities.subs.OfertaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {
    @Query("SELECT o FROM OfertaEntity o WHERE :fecha BETWEEN o.fecha_inicio AND o.fecha_fin")
    Page<OfertaEntity> findActivos(@Param("fecha") LocalDate fecha, Pageable pageable);

}
