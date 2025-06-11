package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.User.ContenidoLike;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContenidoLikeRepository extends JpaRepository<ContenidoLike, Long> {
    boolean existsByUsuarioAndContenido(UsuarioEntity usuario, ContenidoEntity contenido);
    Optional<ContenidoLike> findByUsuarioAndContenido(UsuarioEntity usuario, ContenidoEntity contenido);
    long countByContenido(ContenidoEntity contenido);

    Page<ContenidoLike> findAllByUsuario(UsuarioEntity usuario, Pageable pageable);
}
