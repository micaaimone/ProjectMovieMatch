package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.RatingDTO;
import com.example.demo.model.entities.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {
    @Autowired
    private ModelMapper modelMapper;

    public RatingDTO convertToDTO(RatingEntity ratingEntity)
    {
        return modelMapper.map(ratingEntity, RatingDTO.class);
    }

    public RatingEntity convertToEntity(RatingDTO ratingDTO)
    {
        return modelMapper.map(ratingDTO,RatingEntity.class);
    }
}
