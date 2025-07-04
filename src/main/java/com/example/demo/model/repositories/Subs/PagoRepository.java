package com.example.demo.model.repositories.Subs;

import com.example.demo.model.entities.subs.PagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
    @Query("SELECT p from PagoEntity p where p.suscripcion.id_suscripcion = :id")
    Page<PagoEntity> findBySuscripcionId(@Param("id") Long id, Pageable pageable);

    boolean existsByIdMP(Long idMP);
}
