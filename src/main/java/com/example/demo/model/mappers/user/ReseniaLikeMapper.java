package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Resenia.ContenidoLikeDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaLikeDTO;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.ReseniaLikeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReseniaLikeMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ReseniaLikeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ContenidoLikeDTO convertToDTO(ContenidoLikeEntity contenidoLikeEntity)
    {
        return ContenidoLikeDTO.builder()
                .id(contenidoLikeEntity.getId())
                .idUsuario(contenidoLikeEntity.getUsuario().getId())
                .username(contenidoLikeEntity.getUsuario().getUsername())
                .titulo(contenidoLikeEntity.getContenido().getTitulo())
                .idContenido(contenidoLikeEntity.getContenido().getId_contenido())
                .fechaLike(contenidoLikeEntity.getFechaLike())
                .build();
    }

    public ReseniaLikeDTO convertToDTOResenia(ReseniaLikeEntity reseniaLikeEntity)
    {
        return ReseniaLikeDTO.builder()
                .id(reseniaLikeEntity.getId())
                .idUsuario(reseniaLikeEntity.getUsuario().getId())
                .username(reseniaLikeEntity.getUsuario().getUsername())
                .idResenia(reseniaLikeEntity.getResenia().getId_resenia())
                .titulo(reseniaLikeEntity.getResenia().getContenido().getTitulo())
                .puntuacion(reseniaLikeEntity.getResenia().getPuntuacionU())
                .fechaLike(reseniaLikeEntity.getFechaLike())
                .build();
    }
}
