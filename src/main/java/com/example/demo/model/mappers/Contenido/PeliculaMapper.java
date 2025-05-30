package com.example.demo.model.mappers.Contenido;


import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.ReseñaDTO;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PeliculaMapper {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ReseñaMapper reseñaMapper;

    public PeliculaDTO convertToDTO(PeliculaEntity peliculaEntity)
    {
        PeliculaDTO dto = modelMapper.map(peliculaEntity, PeliculaDTO.class);

        dto.setPuntuacionApi(peliculaEntity.getPuntuacion());

        if (peliculaEntity.getReseña() != null)
        {
            List<ReseñaDTO> reseñasDTO = peliculaEntity.getReseña()
                    .stream()
                    .map(reseñaMapper::convertToDTO)
                    .collect(Collectors.toList());
            dto.setReseña(reseñasDTO);
        }


        return dto;
    }

    public PeliculaEntity convertToEntity(PeliculaDTO peliculaDTO)
    {
        PeliculaEntity entity = modelMapper.map(peliculaDTO, PeliculaEntity.class);

        return entity;
    }
}
