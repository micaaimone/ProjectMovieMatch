package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.ReseñaDTO;
import com.example.demo.model.entities.ReseñaEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReseñaMapper {
    @Autowired
    private ModelMapper modelMapper;

    public ReseñaDTO convertToDTO(ReseñaEntity reseñaEntity)
    {
        return modelMapper.map(reseñaEntity, ReseñaDTO.class);
    }

    public ReseñaEntity convertToEntity(ReseñaDTO reseñaDTO)
    {
        return modelMapper.map(reseñaDTO,ReseñaEntity.class);
    }
}
