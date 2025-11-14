package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.User.AmistadEntity;
import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmistadRepository extends JpaRepository<AmistadEntity, Long> {

    Optional<AmistadEntity> findByIdEmisorAndIdReceptor(Long idEmisor, Long idReceptor);

    Page<AmistadEntity> findAllByIdEmisor(Long idEmisor, Pageable pageable);

    Page<AmistadEntity> findAllByIdReceptorAndEstadoSolicitud(Long idReceptor, EstadoSolicitud estadoSolicitud, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM solicitudes a WHERE (a.idEmisor = :id1 AND a.idReceptor = :id2) OR (a.idEmisor = :id2 AND a.idReceptor = :id1)")
    void deleteAmistadBidireccional(@Param("id1") long id1, @Param("id2") long id2);
}
