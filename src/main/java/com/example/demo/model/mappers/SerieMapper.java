package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.DTOs.RatingDTO;
import com.example.demo.model.DTOs.SerieDTO;
import com.example.demo.model.entities.PeliculaEntity;
import com.example.demo.model.entities.RatingEntity;
import com.example.demo.model.entities.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SerieMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SerieDTO convertToDTO(SerieEntity serieEntity)
    {
        SerieDTO dto = modelMapper.map(serieEntity, SerieDTO.class);

        if (serieEntity.getRatings() != null) {
            List<RatingDTO> ratingDTOs = serieEntity.getRatings()
                    .stream()
                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
                    .toList();

            dto.setRatings(ratingDTOs);
        }

        return dto;
    }

    public SerieEntity convertToEntity(SerieDTO serieDTO)
    {
        SerieEntity entity = modelMapper.map(serieDTO, SerieEntity.class);

        if (serieDTO.getRatings() != null) {
            List<RatingEntity> ratingEntities = serieDTO.getRatings()
                    .stream()
                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
                    .toList();

            entity.setRatings(ratingEntities);
        }

        return entity;
    }
}
