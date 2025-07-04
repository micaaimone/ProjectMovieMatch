package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContenidoLikeRepository extends JpaRepository<ContenidoLikeEntity, Long> {
    boolean existsByUsuarioAndContenido(UsuarioEntity usuario, ContenidoEntity contenido);

    Optional<ContenidoLikeEntity> findByUsuarioAndContenido(UsuarioEntity usuario, ContenidoEntity contenido);

    Page<ContenidoLikeEntity> findAllByContenido(ContenidoEntity contenido, Pageable pageable);
    @Query("SELECT DISTINCT c.contenido.genero FROM ContenidoLikeEntity c WHERE c.usuario.id = :usuarioId")
    List<String> obtenerGenerosLikeadosPorUsuario(@Param("usuarioId") Long usuarioId);

    Page<ContenidoLikeEntity> findAllByUsuario(@PathVariable UsuarioEntity usuario, Pageable pageable);
}
