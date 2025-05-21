package com.example.demo.model.mappers;


import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.DTOs.RatingDTO;
import com.example.demo.model.entities.PeliculaEntity;
import com.example.demo.model.entities.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PeliculaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PeliculaDTO convertToDTO(PeliculaEntity peliculaEntity)
    {
        PeliculaDTO dto = modelMapper.map(peliculaEntity, PeliculaDTO.class);

        if (peliculaEntity.getRatings() != null) {
            List<RatingDTO> ratingDTOs = peliculaEntity.getRatings()
                    .stream()
                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
                    .toList();

            dto.setRatings(ratingDTOs);
        }

        return dto;
    }

    public PeliculaEntity convertToEntity(PeliculaDTO peliculaDTO)
    {
        PeliculaEntity entity = modelMapper.map(peliculaDTO, PeliculaEntity.class);

        if (peliculaDTO.getRatings() != null) {
            List<RatingEntity> ratingEntities = peliculaDTO.getRatings()
                    .stream()
                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
                    .toList();

            entity.setRatings(ratingEntities);
        }

        return entity;
    }
}
