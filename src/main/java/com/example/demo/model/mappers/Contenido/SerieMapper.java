package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class SerieMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SerieDTO convertToDTO(SerieEntity serieEntity)
    {
        SerieDTO dto = modelMapper.map(serieEntity, SerieDTO.class);

        if (serieEntity.getRatings() != null) {

            dto.setPuntuacionApi(serieEntity.getPuntuacion());
        }

        return dto;
    }

    public SerieEntity convertToEntity(SerieDTO serieDTO)
    {
        SerieEntity entity = modelMapper.map(serieDTO, SerieEntity.class);

        return entity;
    }
}
