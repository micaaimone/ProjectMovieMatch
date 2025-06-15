package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.user.Grupo.ModificarGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.User.GrupoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrupoMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public GrupoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GrupoEntity convertToEntity(NewGrupoDTO newGrupoDTO) {
        return modelMapper.map(newGrupoDTO, GrupoEntity.class);
    }

    public VisualizarGrupoDTO convertToVisualizarGrupo(GrupoEntity grupoEntity) {
        return VisualizarGrupoDTO.builder()
                .nombre(grupoEntity.getNombre())
                .descripcion(grupoEntity.getDescripcion()) // si es null, queda null, no hace falta if
                .build();
    }



}
