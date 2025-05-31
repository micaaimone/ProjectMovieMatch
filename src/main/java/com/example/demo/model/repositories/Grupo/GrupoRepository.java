package com.example.demo.model.repositories.Grupo;

import com.example.demo.model.entities.GrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {
    List<GrupoEntity> findByAdministradorId(Long idAdministrador);

}
