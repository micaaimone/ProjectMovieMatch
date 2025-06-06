package com.example.demo.model.repositories.Usuarios;

import com.example.demo.model.entities.User.ListasContenidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface ListasContenidoRepository extends JpaRepository<ListasContenidoEntity, Long> {
    @Query("SELECT p FROM ListasContenidoEntity p WHERE p.usuario.id = :idUser")
    Page<ListasContenidoEntity> findByIdUser(@PathVariable("id") Long idUser, Pageable pageable);
    @Query("SELECT p FROM ListasContenidoEntity p where p.usuario.id = :id AND p.nombre = :nombre")
    Optional<ListasContenidoEntity> findByNombre(@PathVariable("id")Long id, @Param("nombre")String nombre);
    @Query("SELECT p from ListasContenidoEntity p where p.usuario.username = :username AND p.privado = false")
    Page<ListasContenidoEntity> listaOtroUser(@PathVariable("username") String username, Pageable pageable);
}
