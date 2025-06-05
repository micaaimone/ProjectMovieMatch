package com.example.demo.model.repositories;

import com.example.demo.model.entities.SolicitudAmistadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitudAmistadRepository  extends JpaRepository<SolicitudAmistadEntity, Long> {

    Optional<SolicitudAmistadEntity> findByIdEmisorAndIdReceptor(Long idEmisor, Long idReceptor);

    Page<SolicitudAmistadEntity> findAllByIdEmisor(Long idEmisor, Pageable pageable);
}
