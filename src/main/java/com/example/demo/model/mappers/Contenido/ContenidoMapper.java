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
//            List<RatingDTO> ratingDTOs = contenidoEntity.getRatings()
//                    .stream()
//                    .map(rating -> modelMapper.map(rating, RatingDTO.class))
//                    .toList();
//
//            dto.setRatings(ratingDTOs);


            double promedio = contenidoEntity.getRatings()
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

    public ContenidoEntity convertToEntity(ContenidoDTO contenidoDTO)
    {
        ContenidoEntity entity = modelMapper.map(contenidoDTO, PeliculaEntity.class);

//        if (contenidoDTO.getRatings() != null) {
//            List<RatingEntity> ratingEntities = contenidoDTO.getRatings()
//                    .stream()
//                    .map(ratingDTO -> modelMapper.map(ratingDTO, RatingEntity.class))
//                    .toList();
//
//            entity.setRatings(ratingEntities);
//        }

        return entity;
    }
}
