package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.ContenidoDTO;
import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.DTOs.RatingDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.PeliculaEntity;
import com.example.demo.model.entities.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContenidoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public ContenidoDTO convertToDTO(ContenidoEntity contenidoEntity)
    {
        ContenidoDTO dto = modelMapper.map(contenidoEntity, PeliculaDTO.class);

        if (contenidoEntity.getRatings() != null) {
            List<RatingDTO> ratingDTOs = contenidoEntity.getRatings()
                    .stream()
                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
                    .toList();

            dto.setRatings(ratingDTOs);
        }

        return dto;
    }

    public ContenidoEntity convertToEntity(ContenidoDTO contenidoDTO)
    {
        ContenidoEntity entity = modelMapper.map(contenidoDTO, PeliculaEntity.class);

        if (contenidoDTO.getRatings() != null) {
            List<RatingEntity> ratingEntities = contenidoDTO.getRatings()
                    .stream()
                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
                    .toList();

            entity.setRatings(ratingEntities);
        }

        return entity;
    }
}
