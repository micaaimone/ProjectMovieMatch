package com.example.demo.model.DTOs.user;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.ContenidoMostrarAdminDTO;
import com.example.demo.model.DTOs.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.entities.User.CredencialEntity;
import com.example.demo.model.entities.User.ListasContenidoEntity;
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
    private List<ContenidoMostrarAdminDTO> likes;
    private List<ReseniaMostrarUsuarioDTO> reseñas;
    private List<ListaContenidoDTO> listas;
}
