package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.User.GrupoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GrupoMapper {
    private final ModelMapper modelMapper;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public GrupoMapper(ModelMapper modelMapper, UsuarioMapper usuarioMapper) {
        this.modelMapper = modelMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public GrupoEntity convertToEntity(NewGrupoDTO newGrupoDTO) {
        return modelMapper.map(newGrupoDTO, GrupoEntity.class);
    }

    public VisualizarGrupoDTO convertToVisualizarGrupo(GrupoEntity grupoEntity) {
        return VisualizarGrupoDTO.builder()
                .idGrupo(grupoEntity.getIdGrupo())
                .nombre(grupoEntity.getNombre())
                .descripcion(grupoEntity.getDescripcion())
                .admin(usuarioMapper.convertUsuarioGrupoDTO(grupoEntity.getAdministrador()))
                .usuarios(grupoEntity.getListaUsuarios().stream()
                        .map(usuarioMapper::convertUsuarioGrupoDTO) // convertís cada UsuarioEntity a DTO
                        .collect(Collectors.toSet()))
                .build();
    }



}
