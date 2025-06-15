package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import java.util.Set;

import com.example.demo.model.DTOs.user.*;
import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.user.Grupo.UsuarioGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.DTOs.user.Listas.ListaContenidoDTO;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.ReseniaLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.Contenido.ReseniaMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;
    private final ReseniaMapper reseniaMapper;
    private final ContenidoMapper contenidoMapper;
    private final ListasMapper listasMapper;
    private final GrupoMapper grupoMapper;

    @Autowired
    public UsuarioMapper(ModelMapper modelMapper, ReseniaMapper reseniaMapper, ContenidoMapper contenidoMapper, ListasMapper listasMapper, GrupoMapper grupoMapper) {
        this.modelMapper = modelMapper;
        this.reseniaMapper = reseniaMapper;
        this.contenidoMapper = contenidoMapper;
        this.listasMapper = listasMapper;
        this.grupoMapper = grupoMapper;
    }

    public AmigoDTO convertAmigoToDTO(UsuarioEntity usuarioEntity) {
        return modelMapper.map(usuarioEntity, AmigoDTO.class);
    }

    public UsuarioGrupoDTO convertUsuarioGrupoDTO(UsuarioEntity usuarioEntity)
    {
        return modelMapper.map(usuarioEntity, UsuarioGrupoDTO.class);
    }

    public UsuarioDTO convertToDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO dto = modelMapper.map(usuarioEntity, UsuarioDTO.class);

        if (usuarioEntity.getContenidoLikes() != null) {
            List<ContenidoMostrarDTO> contenidoDTOS = usuarioEntity.getContenidoLikes()
                    .stream()
                    .map(ContenidoLikeEntity::getContenido)
                    .map(contenidoMapper::convertToDTOForAdmin)
                    .toList();
            dto.setContenidoLikes(contenidoDTOS);
        }

        if (usuarioEntity.getReseniaLikes() != null) {
            List<ReseniaMostrarUsuarioDTO> reseniaDTOS = usuarioEntity.getReseniaLikes()
                    .stream()
                    .map(ReseniaLikeEntity::getResenia)
                    .map(reseniaMapper::convertToDTOUsuario)
                    .toList();
            dto.setReseniaLikes(reseniaDTOS);
        }

        if (usuarioEntity.getAmigos() != null) {
            List<AmigoDTO> amigosDTOs = usuarioEntity.getAmigos()
                    .stream()
                    .map(this::convertAmigoToDTO)
                    .toList();
            dto.setAmigos(amigosDTOs);
        }

        if (usuarioEntity.getReseñasHechas() != null) {
            List<ReseniaMostrarUsuarioDTO> reseniasDTO = usuarioEntity.getReseñasHechas()
                    .stream()
                    .map(reseniaMapper::convertToDTOUsuario)
                    .toList();
            dto.setResenias(reseniasDTO);
        }

        if (usuarioEntity.getListas() != null) {
            List<ListaContenidoDTO> listasDTO = usuarioEntity.getListas()
                    .stream()
                    .map(listasMapper::convertToDTO)
                    .toList();
            dto.setListas(listasDTO);
        }

        if (usuarioEntity.getGrupos() != null)
        {
            Set<VisualizarGrupoDTO> grupoDTOS = usuarioEntity.getGrupos()
                    .stream()
                    .map (grupoMapper::convertToVisualizarGrupo)
                    .collect(Collectors.toSet());
            dto.setGrupos(grupoDTOS);
        }



        Set<String> roles = usuarioEntity.getCredencial().getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toSet());


        CredentialDTOForUser credentialDTO = new CredentialDTOForUser(usuarioEntity.getCredencial().getEmail(), roles);

        dto.setCredencial(credentialDTO);



        return dto;
    }

    public UsuarioEntity convertToNewEntity(NewUsuarioDTO usuarioDTO) {
        return UsuarioEntity.builder()
                .edad(usuarioDTO.getEdad())
                .apellido(usuarioDTO.getApellido())
                .nombre(usuarioDTO.getNombre())
                .telefono(usuarioDTO.getTelefono())
                .username(usuarioDTO.getUsername())
                .activo(true)
                .generos(usuarioDTO.getGeneros())
                .build();
    }

    public UsuarioEntity convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioEntity.class);
    }
}
