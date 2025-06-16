package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long>, JpaSpecificationExecutor<ContenidoEntity> {

    @Query("SELECT c FROM contenido c " +
            "JOIN c.likes l1 " +
            "JOIN c.likes l2 " +
            "WHERE l1.usuario.id IN :grupo AND l2.usuario.id IN :grupo " +
            "AND l1.usuario.id <> l2.usuario.id " +
            "GROUP BY c.id_contenido " +
            "HAVING COUNT(DISTINCT l1.usuario.id) >= 2")
    Page<ContenidoEntity> obtenerMatchDeGrupo(@Param("grupo") List<Long> idsUsuarios, Pageable pageable);

    @Query("SELECT c FROM contenido c " +
            "WHERE c.genero IN :generosUsuario " +
            "AND c.clasificacion <= :edadUsuario")
    Page<ContenidoEntity> recomendarContenidoPorGeneroYEdad(
            @Param("generosUsuario") List<String> generos,
            @Param("edadUsuario") int edad,
            Pageable pageable);
}