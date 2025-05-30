package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.DTOs.ReseñaDTO;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class SerieMapper {

    @Autowired
    private ModelMapper modelMapper;
    private ReseñaMapper reseñaMapper;

    public SerieDTO convertToDTO(SerieEntity serieEntity)
    {
        SerieDTO dto = modelMapper.map(serieEntity, SerieDTO.class);

        dto.setPuntuacionApi(serieEntity.getPuntuacion());

        if (serieEntity.getReseña() != null)
        {
            List<ReseñaDTO> reseñasDTO = serieEntity.getReseña()
                    .stream()
                    .map(reseñaMapper::convertToDTO)
                    .collect(Collectors.toList());
            dto.setReseña(reseñasDTO);
        }

        return dto;
    }

    public SerieEntity convertToEntity(SerieDTO serieDTO)
    {
        SerieEntity entity = modelMapper.map(serieDTO, SerieEntity.class);

        return entity;
    }
}
