package com.example.demo.model.repositories;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

     Page<UsuarioEntity> findByEdadGreaterThan(Long edad, Pageable pageable);

     Optional<UsuarioEntity> findByUsername(String username);
     boolean existsByEmail(String email);
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("SELECT u.likes FROM UsuarioEntity u JOIN u.likes WHERE u.id = :usuarioId")
    Page<ContenidoEntity> findLikes(@Param("usuarioId")Long usuarioId, Pageable pageable);
}
