package com.example.demo.model.DTOs.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.entities.User.CredencialEntity;
import lombok.*;


import java.util.List;

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
    private List<ContenidoMostrarDTO> likes;
    private List<ReseniaMostrarUsuarioDTO> reseñas;
    private List<ListaContenidoDTO> listas;
}
