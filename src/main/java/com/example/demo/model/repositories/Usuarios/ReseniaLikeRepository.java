package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.ReseniaLike;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReseniaLikeRepository extends JpaRepository<ReseniaLike, Long> {
    boolean existsByUsuarioAndResenia(UsuarioEntity usuario, ReseniaEntity resenia);
    Optional<ReseniaLike> findByUsuarioAndResenia(UsuarioEntity usuario, ReseniaEntity resenia);
    long countByResenia(ReseniaEntity resenia);

    Page<ReseniaLike> findAllByUsuario(UsuarioEntity usuario, Pageable pageable);
}
