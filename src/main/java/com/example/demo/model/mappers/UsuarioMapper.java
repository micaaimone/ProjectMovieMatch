package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.UsuarioEntity;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioMapper implements Mapper<UsuarioEntity, UsuarioDTO> {

    @Autowired
    public UsuarioDTO convertToDTO(UsuarioEntity usuarioEntity) {
        return modelMapper.map(usuarioEntity, UsuarioDTO.class);
    }

    public UsuarioEntity convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioEntity.class);
    }
}
