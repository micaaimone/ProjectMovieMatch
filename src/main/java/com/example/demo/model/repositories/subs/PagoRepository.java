package com.example.demo.model.repositories.subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.entities.subs.PagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
    @Query("SELECT p from SuscripcionEntity p where p.id_suscripcion = :id")
    Page<PagoEntity> findBySuscripcionId(@Param("id") Long id, Pageable pageable);
}
