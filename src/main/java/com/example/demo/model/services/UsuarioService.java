package com.example.demo.model.services;

import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
}
