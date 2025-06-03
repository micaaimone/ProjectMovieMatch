package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.ReseniaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReseniaRepository extends JpaRepository<ReseniaEntity, Long> {

//    @Query("SELECT r FROM ReseniaEntity r WHERE r.usuario.id = :id_usuario AND r.contenido.id_contenido = :id_contenido AND r.usuario.activo = true AND r.contenido.activo = true")
//    Optional<ReseniaEntity> findByIDAndContenido(@Param("id_usuario") Long id_usuario, @Param("id_contenido") Long id_contenido);
//
//    @Query("SELECT r FROM ReseniaEntity r WHERE r.usuario.id = :id_usuario")
//    Page<ReseniaEntity> findAllById(Long id_usuario, Pageable pageable);

    @Query("SELECT r FROM reseña r WHERE r.usuario.id = :id_usuario AND r.contenido.id_contenido = :id_contenido")
    Optional<ReseniaEntity> findByIDAndContenido(@Param("id_usuario") Long id_usuario, @Param("id_contenido") Long id_contenido);

    @Query("SELECT r FROM reseña r WHERE r.usuario.id = :id_usuario")
    Page<ReseniaEntity> findAllById(Long id_usuario, Pageable pageable);
}
