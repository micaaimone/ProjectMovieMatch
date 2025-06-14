package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.ReseniaLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReseniaLikeRepository extends JpaRepository<ReseniaLikeEntity, Long> {
    boolean existsByUsuarioAndResenia(UsuarioEntity usuario, ReseniaEntity resenia);
    Optional<ReseniaLikeEntity> findByUsuarioAndResenia(UsuarioEntity usuario, ReseniaEntity resenia);
    long countByResenia(ReseniaEntity resenia);

    @Query("SELECT rl FROM ReseniaLikeEntity rl WHERE rl.usuario.id = :usuarioId")
    Page<ReseniaLikeEntity> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Page<ReseniaLikeEntity> findAllByUsuario(UsuarioEntity usuario, Pageable pageable);
}
