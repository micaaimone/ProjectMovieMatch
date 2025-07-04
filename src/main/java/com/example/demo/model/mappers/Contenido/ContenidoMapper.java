package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoCompletoDTO;
import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContenidoMapper {
    private final ModelMapper modelMapper;
    private final ReseniaMapper reseñaMapper;

    @Autowired
    public ContenidoMapper(ModelMapper modelMapper, ReseniaMapper reseñaMapper) {
        this.modelMapper = modelMapper;
        this.reseñaMapper = reseñaMapper;
    }

    public ContenidoMostrarDTO convertToDTOForAdmin(ContenidoEntity contenidoEntity)
    {
        return ContenidoMostrarDTO.builder()
                .id(contenidoEntity.getId_contenido())
                .titulo(contenidoEntity.getTitulo())
                .tipo(contenidoEntity.getTipo())
                .build();
    }

    public ContenidoDTO convertToDTO(ContenidoEntity contenidoEntity)
    {
        ContenidoDTO dto = modelMapper.map(contenidoEntity, ContenidoDTO.class);

        dto.setPuntuacionApi(contenidoEntity.getPuntuacion());

        if (contenidoEntity.getReseña() != null)
        {
            //mapeo las reseñas, las hago dto
            List<ReseniaDTO> reseñasDTO = contenidoEntity.getReseña()
                    .stream()
                    .map(reseñaMapper::convertToDTO)
                    .collect(Collectors.toList());
            dto.setReseña(reseñasDTO);

            //saco el promedio de cada peli
            double promedioReseña= contenidoEntity.getReseña()
                    .stream()
                    .mapToDouble(ReseniaEntity::getPuntuacionU)
                    .average()
                    .orElse(0.0);

            dto.setPromedioPuntuacionUsuario(promedioReseña);
        }

        return dto;
    }

    public ContenidoEntity convertToEntity(ContenidoDTO contenidoDTO)
    {
        return modelMapper.map(contenidoDTO, ContenidoEntity.class);
    }

    public ContenidoEntity convertFromContenidoCompleltoToEntity(ContenidoCompletoDTO contenidoDTO)
    {
        return modelMapper.map(contenidoDTO, ContenidoEntity.class);
    }

}
