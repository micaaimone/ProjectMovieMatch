package com.example.demo.model.DTOs.user;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.CredencialEntity;
import lombok.*;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioDTO {
    private String username;
    private String email;
    private CredencialEntity credencial;
    private Boolean activo;
    //  private SuscripcionEntity suscripcion;
    private Set<ContenidoEntity> likes;
    private List<ReseniaMostrarUsuarioDTO> reseñas;
}
