package com.example.demo.model.DTOs;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.CredencialEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String username;
    private String email;
    private CredencialEntity credencial;
    //  private SuscripcionEntity suscripcion;
    private Set<ContenidoEntity> likes;
}
