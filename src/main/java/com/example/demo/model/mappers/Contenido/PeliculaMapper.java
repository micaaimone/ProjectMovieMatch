package com.example.demo.model.mappers.Contenido;


import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class PeliculaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PeliculaDTO convertToDTO(PeliculaEntity peliculaEntity)
    {
        PeliculaDTO dto = modelMapper.map(peliculaEntity, PeliculaDTO.class);

        if (peliculaEntity.getRatings() != null) {

            dto.setPuntuacionApi(peliculaEntity.getPuntuacion());
        }


        return dto;
    }

    public PeliculaEntity convertToEntity(PeliculaDTO peliculaDTO)
    {
        PeliculaEntity entity = modelMapper.map(peliculaDTO, PeliculaEntity.class);

        return entity;
    }
}
