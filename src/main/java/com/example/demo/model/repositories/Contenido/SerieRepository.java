package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<SerieEntity, Long> {
    Page<SerieEntity> findByEstado(int estado, Pageable pageable);

    Page<SerieEntity> findByEstadoOrderByPuntuacionDesc(int estado, Pageable pageable);

}
