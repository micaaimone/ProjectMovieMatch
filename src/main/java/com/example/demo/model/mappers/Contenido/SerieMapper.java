package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SerieMapper {

    private final ModelMapper modelMapper;
    private final ReseniaMapper reseñaMapper;

    @Autowired
    public SerieMapper(ModelMapper modelMapper, ReseniaMapper reseñaMapper) {
        this.modelMapper = modelMapper;
        this.reseñaMapper = reseñaMapper;
    }

    public SerieDTO convertToDTO(SerieEntity serieEntity)
    {
        SerieDTO dto = modelMapper.map(serieEntity, SerieDTO.class);

        dto.setPuntuacionApi(serieEntity.getPuntuacion());

        if (serieEntity.getReseña() != null)
        {
            List<ReseniaDTO> reseñasDTO = serieEntity.getReseña()
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
