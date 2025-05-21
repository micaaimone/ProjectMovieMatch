package com.example.demo.model.repositories;

import com.example.demo.model.entities.subs.SuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SuscripcionRepository extends JpaRepository<SuscripcionEntity, Long> {

    @Query(value = "SELECT * FROM suscripciones WHERE estado = 1", nativeQuery = true)
    List<SuscripcionEntity> findActivos();

    @Modifying
    @Transactional
    @Query("UPDATE SuscripcionEntity s SET s.estado = true WHERE s.idSuscripcion = :id")
    void activarSub(@Param("id") Long id);

}
