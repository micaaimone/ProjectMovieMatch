package com.example.demo.model.services.Usuarios;

import com.example.demo.model.entities.CredencialEntity;

import com.example.demo.model.repositories.Usuarios.CredencialRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CredencialService {
    private final CredencialRepository credencialRepository;

    public CredencialService(CredencialRepository credencialRepository) {
        this.credencialRepository = credencialRepository;
    }

    //cambiar por DTO
    public Page<CredencialEntity> findAll(Pageable pageable){
        return credencialRepository.findAll(pageable);
    }

    public CredencialEntity findById(Long id){
        return credencialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }
    public CredencialEntity save(CredencialEntity credencialEntity){return credencialRepository.save(credencialEntity);}

    // agregar boolean?
    public void delete(CredencialEntity credencialEntity){}
}
