package com.example.demo.Seguridad.repositories;

import com.example.demo.Seguridad.Entities.PermitEntity;
import com.example.demo.Seguridad.Enum.Permit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermitRepository extends JpaRepository<PermitEntity, Long> {
    Optional<PermitEntity> findByPermit(Permit permit);
}

