package com.example.demo.model.mappers.Contenido;


import com.example.demo.model.DTOs.Contenido.ContenidoCompletoDTO;
import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PeliculaMapper {

    private final ModelMapper modelMapper;
    private final ReseniaMapper reseñaMapper;

    @Autowired
    public PeliculaMapper(ModelMapper modelMapper, ReseniaMapper reseñaMapper) {
        this.modelMapper = modelMapper;
        this.reseñaMapper = reseñaMapper;
    }

    public PeliculaDTO convertToDTO(PeliculaEntity peliculaEntity)
    {
        PeliculaDTO dto = modelMapper.map(peliculaEntity, PeliculaDTO.class);

        dto.setPuntuacionApi(peliculaEntity.getPuntuacion());

        if (peliculaEntity.getReseña() != null)
        {
            List<ReseniaDTO> reseñasDTO = peliculaEntity.getReseña()
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

    public PeliculaEntity convertFromContenidoCompletoToEntity(ContenidoCompletoDTO dto) {
        return modelMapper.map(dto, PeliculaEntity.class);
    }
}
