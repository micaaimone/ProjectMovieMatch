package com.example.demo.model.DTOs.user;

import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.DTOs.user.Listas.ListaContenidoDTO;
import com.example.demo.model.enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "DTO que representa los datos del usuario autenticado")
public class UsuarioDTO {

    private Long id;

    private String username;

    private CredentialDTOForUser credencial;

    private List<ContenidoMostrarDTO> contenidoLikes;

    private List<ReseniaMostrarUsuarioDTO> reseniaLikes;

    private List<AmigoDTO> amigos;

    private List<ReseniaMostrarUsuarioDTO> resenias;

    private List<ListaContenidoDTO> listas;

    private Set<Genero> generos;

    private Set<VisualizarGrupoDTO> grupos;
}
