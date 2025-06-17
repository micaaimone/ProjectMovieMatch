package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.GrupoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {
    Page<GrupoEntity> findAllByListaUsuarios_Id(Long id, Pageable pageable);

    Optional<GrupoEntity> findByNombre(String nombre);

}