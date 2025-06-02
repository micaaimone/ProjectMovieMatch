package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.ReseniaDTO;
import com.example.demo.model.DTOs.ReseniaModificarDTO;
import com.example.demo.model.DTOs.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.mappers.Contenido.ReseniaMapper;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper{
private final ModelMapper modelMapper;
    private final ReseniaMapper reseñaMapper;


    public UsuarioMapper(ModelMapper modelMapper, ReseniaMapper reseñaMapper) {
        this.modelMapper = modelMapper;
        this.reseñaMapper = reseñaMapper;
    }

    public UsuarioDTO convertToDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO dto = modelMapper.map(usuarioEntity, UsuarioDTO.class);
        if (usuarioEntity.getReseñasHechas() != null) {
            //mapeo las reseñas, las hago dto
            List<ReseniaMostrarUsuarioDTO> reseñasDTO = usuarioEntity.getReseñasHechas()
                    .stream()
                    .map(reseñaMapper::convertToDTOUsuario)
                    .toList();
            dto.setReseñas(reseñasDTO);
        }
        return dto;
    }

    public UsuarioEntity convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioEntity.class);
    }
}
