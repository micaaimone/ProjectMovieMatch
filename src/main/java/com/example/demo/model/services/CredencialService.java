package com.example.demo.model.services;

import com.example.demo.model.entities.CredencialEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.repositories.CredencialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CredencialService {

    private CredencialRepository credencialRepository;

    @Autowired
    public CredencialService(CredencialRepository credencialRepository) {this.credencialRepository = credencialRepository;}

    public List<CredencialEntity> findAll(){
        return credencialRepository.findAll();
    }

    public Optional<CredencialEntity> findById(Long id){
        return credencialRepository.findById(id);
    }
    public CredencialEntity save(CredencialEntity credencialEntity){return credencialRepository.save(credencialEntity);}

    // agregar boolean?
    public void delete(CredencialEntity credencialEntity){}
}
