package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContenidoRepository extends JpaRepository<ContenidoEntity, JpaSpecificationExecutor<ContenidoEntity>> {

    Page<ContenidoEntity> findByEstado(int estado, Pageable pageable);

    Page<ContenidoEntity> findByGeneroAndEstado(String genero, int estado, Pageable pageable);

    Page<ContenidoEntity> findByAnioAndEstado(String anio, int estado, Pageable pageable);

    Page<ContenidoEntity> findByTituloContainingIgnoreCaseAndEstado(String titulo, int estado, Pageable pageable);

    Page<ContenidoEntity> findByEstadoOrderByPuntuacionDesc(int estado, Pageable pageable);

    //crear un metodo q busque por los atributos generales y
}