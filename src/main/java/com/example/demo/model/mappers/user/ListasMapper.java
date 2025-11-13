package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.user.Listas.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.Listas.ListasSinContDTO;
import com.example.demo.model.DTOs.user.Listas.ListaResumenDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ListasContenidoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListasMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ListasMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //dto con contenido
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
    //entity con ocntenido
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

    public ListasSinContDTO convertToDTOSC(ListasContenidoEntity entity) {
        ListasSinContDTO dto = new ListasSinContDTO();

        return modelMapper.map(entity, ListasSinContDTO.class);
    }

    public ListasContenidoEntity convertToEntitySC(ListasSinContDTO dto) {
        ListasContenidoEntity entity = new ListasContenidoEntity();
        return modelMapper.map(dto, ListasContenidoEntity.class);
    }


    public ListaResumenDTO convertToDTOResumen(ListasContenidoEntity entity) {
        ListaResumenDTO dto = new ListaResumenDTO();

        return modelMapper.map(entity, ListaResumenDTO.class);
    }

}
