package com.example.demo.model.repositories.Contenido;

import com.example.demo.model.entities.Contenido.SerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SerieRepository extends JpaRepository<SerieEntity, Long>, JpaSpecificationExecutor<SerieEntity> {

}
