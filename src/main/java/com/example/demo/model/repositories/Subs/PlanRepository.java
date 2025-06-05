package com.example.demo.model.repositories.Subs;

import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<PlanSuscripcionEntity, Long> {
    @Query("select o from PlanSuscripcionEntity o where o.tipo = :tipo")
    Optional<PlanSuscripcionEntity> findByTipo(@Param("tipo") TipoSuscripcion tipo);

}
