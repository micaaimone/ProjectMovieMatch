package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.User.AmistadEntity;
import com.example.demo.model.enums.EstadoSolicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmistadRepository extends JpaRepository<AmistadEntity, Long> {

    Optional<AmistadEntity> findByIdEmisorAndIdReceptor(Long idEmisor, Long idReceptor);

    Page<AmistadEntity> findAllByIdEmisor(Long idEmisor, Pageable pageable);

    Page<AmistadEntity> findAllByIdReceptorAndEstadoSolicitud(Long idReceptor, EstadoSolicitud estadoSolicitud, Pageable pageable);

}
