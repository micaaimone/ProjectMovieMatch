package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ContenidoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public ContenidoDTO convertToDTO(ContenidoEntity contenidoEntity)
    {
        ContenidoDTO dto = modelMapper.map(contenidoEntity, ContenidoDTO.class);

        if (contenidoEntity.getRatings() != null) {
            dto.setPuntuacionApi(contenidoEntity.getPuntuacion());
        }

        return dto;
    }

    public ContenidoEntity convertToEntity(ContenidoDTO contenidoDTO)
    {
        ContenidoEntity entity = modelMapper.map(contenidoDTO, PeliculaEntity.class);

        return entity;
    }
}
