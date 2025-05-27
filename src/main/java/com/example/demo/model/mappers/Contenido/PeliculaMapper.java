package com.example.demo.model.mappers.Contenido;


import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class PeliculaMapper {

    @Autowired
    private ModelMapper modelMapper;

    public PeliculaDTO convertToDTO(PeliculaEntity peliculaEntity)
    {
        PeliculaDTO dto = modelMapper.map(peliculaEntity, PeliculaDTO.class);

        if (peliculaEntity.getRatings() != null) {
//            List<RatingDTO> ratingDTOs = peliculaEntity.getRatings()
//                    .stream()
//                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
//                    .toList();
//
//            dto.setRatings(ratingDTOs);

            double promedio = peliculaEntity.getRatings()
                    .stream()
                    .mapToDouble(r -> {
                        String valor = r.getValor();
                        if (valor == null || valor.isEmpty() || valor.equals("N/A")) {
                            return 0.0;
                        }
                        if (valor.contains("/")) {
                            String[] parts = valor.split("/");
                            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]) * 10;
                        } else if (valor.contains("%")) {
                            return Double.parseDouble(valor.replace("%", "")) / 10;
                        } else {
                            return Double.parseDouble(valor);
                        }
                    })
                    .average()
                    .orElseThrow(() -> new NoSuchElementException("No hay ratings disponibles"));

            dto.setPuntuacionApi(promedio);
        }


        return dto;
    }

    public PeliculaEntity convertToEntity(PeliculaDTO peliculaDTO)
    {
        PeliculaEntity entity = modelMapper.map(peliculaDTO, PeliculaEntity.class);

//        if (peliculaDTO.getRatings() != null) {
//            List<RatingEntity> ratingEntities = peliculaDTO.getRatings()
//                    .stream()
//                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
//                    .toList();
//
//            entity.setRatings(ratingEntities);
//        }

        return entity;
    }
}
