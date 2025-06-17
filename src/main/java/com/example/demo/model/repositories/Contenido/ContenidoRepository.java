package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;

import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.enums.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long>, JpaSpecificationExecutor<ContenidoEntity> {

    @Query("SELECT c FROM contenido c " +
            "JOIN c.likes l " +
            "WHERE l.usuario IN :grupo " +
            "GROUP BY c.id_contenido " +
            "HAVING COUNT(DISTINCT l.usuario) >= 2")
    Page<ContenidoEntity> obtenerMatchDeGrupo(@Param("grupo") Set<UsuarioEntity> grupo, Pageable pageable);


    @Query("SELECT c FROM contenido c " +
            "WHERE c.genero IN :generosUsuario")
    Page<ContenidoEntity> recomendarContenidoPorGeneroYEdad(
            @Param("generosUsuario") List<String> generos,
            Pageable pageable);

}