package com.example.demo.model.repositories.Subs;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SuscripcionRepository extends JpaRepository<SuscripcionEntity, Long> {

    @Query(value = "SELECT * FROM suscripciones WHERE estado = 1", nativeQuery = true)
    Page<SuscripcionEntity> findActivos(Pageable pageable);

    @Query("SELECT s FROM SuscripcionEntity s where s.fecha_fin > :now and s.estado = true")
    List<SuscripcionEntity> bajarSub(@Param("now") LocalDate now);

    @Query("SELECT u FROM SuscripcionEntity u WHERE u.estado = true AND u.fecha_fin = :fecha")
    List<SuscripcionEntity> porVencer(@Param("fecha") LocalDate fecha);

}
