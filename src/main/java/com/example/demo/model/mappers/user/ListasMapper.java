package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.subs.ListasContenidoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListasMapper {
    private final ModelMapper modelMapper;
    public ListasMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ListaContenidoDTO convertToDTO(ListasContenidoEntity entity) {
        ListaContenidoDTO dto = new ListaContenidoDTO();
        dto = modelMapper.map(entity, ListaContenidoDTO.class);

        if(dto.getContenidos() != null) {
            List<ContenidoDTO> dtos = entity.getContenidos()
                    .stream()
                    .map(contenidoEntity -> modelMapper.map(contenidoEntity, ContenidoDTO.class))
                    .toList();
            dto.setContenidos(dtos);

        }
        return dto;
    }

    public ListasContenidoEntity convertToEntity(ListaContenidoDTO dto) {
        ListasContenidoEntity entity = new ListasContenidoEntity();
        entity = modelMapper.map(dto, ListasContenidoEntity.class);
        if(dto.getContenidos() != null) {
            List<ContenidoEntity> dtos = dto.getContenidos()
                    .stream()
                    .map(contenidoDTO -> modelMapper.map(contenidoDTO, ContenidoEntity.class))
                    .toList();
            entity.setContenidos(dtos);
        }
        return entity;
    }

}
