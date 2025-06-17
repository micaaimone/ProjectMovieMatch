package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;

import com.example.demo.model.enums.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long>, JpaSpecificationExecutor<ContenidoEntity> {

    @Query("SELECT c FROM contenido c " +
            "JOIN c.likes l " +
            "WHERE l.usuario.id IN :grupo " +
            "GROUP BY c.id_contenido " +
            "HAVING COUNT(DISTINCT l.usuario.id) >= 2")
    Page<ContenidoEntity> obtenerMatchDeGrupo(@Param("grupo") List<Long> grupo, Pageable pageable);

    @Query("SELECT c FROM contenido c " +
            "WHERE c.genero IN :generosUsuario " +
            "AND c.clasificacion <= :edadUsuario")
    Page<ContenidoEntity> recomendarContenidoPorGeneroYEdad(
            @Param("generosUsuario") List<String> generos,
            @Param("edadUsuario") int edad,
            Pageable pageable);
}