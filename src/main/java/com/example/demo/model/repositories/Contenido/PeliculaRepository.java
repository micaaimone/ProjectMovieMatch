package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeliculaRepository extends JpaRepository<PeliculaEntity, Long> {
    Page<PeliculaEntity> findByEstado(int estado, Pageable pageable);
    Page<PeliculaEntity> findByEstadoOrderByPuntuacionDesc(int estado, Pageable pageable);

}
