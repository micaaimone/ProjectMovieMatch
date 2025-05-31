package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long>, JpaSpecificationExecutor<ContenidoEntity> {
}