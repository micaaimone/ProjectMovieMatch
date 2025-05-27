package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.RatingDTO;
import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class SerieMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SerieDTO convertToDTO(SerieEntity serieEntity)
    {
        SerieDTO dto = modelMapper.map(serieEntity, SerieDTO.class);

        if (serieEntity.getRatings() != null) {
//            List<RatingDTO> ratingDTOs = serieEntity.getRatings()
//                    .stream()
//                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
//                    .toList();
//
//            dto.setRatings(ratingDTOs);

            double promedio = serieEntity.getRatings()
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

    public SerieEntity convertToEntity(SerieDTO serieDTO)
    {
        SerieEntity entity = modelMapper.map(serieDTO, SerieEntity.class);

//        if (serieDTO.getRatings() != null) {
//            List<RatingEntity> ratingEntities = serieDTO.getRatings()
//                    .stream()
//                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
//                    .toList();
//
//            entity.setRatings(ratingEntities);
//        }

        return entity;
    }
}
