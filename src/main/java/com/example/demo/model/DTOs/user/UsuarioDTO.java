package com.example.demo.model.DTOs.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.enums.Genero;
import lombok.*;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioDTO {
    private Long id;
    private String username;
    private CredentialDTOForUser credencial;
    private Boolean activo;
    private List<ContenidoMostrarDTO> contenidoLikes;
    private List<ReseniaMostrarUsuarioDTO> reseniaLikes;
    private List<AmigoDTO> amigos;
    private List<ReseniaMostrarUsuarioDTO> resenias;
    private List<ListaContenidoDTO> listas;
    private Set<Genero> generos;
}