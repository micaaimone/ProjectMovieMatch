package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.SolicitudAmistadDTO;
import com.example.demo.model.entities.SolicitudAmistadEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SolicitudAmistadMapper {

    private final ModelMapper modelMapper;

    public SolicitudAmistadMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SolicitudAmistadEntity convertToNewEntity(NewSolicitudAmistadDTO newSolicitudAmistadDTO)
    {
        return SolicitudAmistadEntity.builder()
                .idEmisor(newSolicitudAmistadDTO.getIdEmisor())
                .idReceptor(newSolicitudAmistadDTO.getIdReceptor())
                .fechaSolicitud((LocalDateTime.now()))
                .build();
    }

    public SolicitudAmistadDTO convertToDTO(SolicitudAmistadEntity solicitudAmistad)
    {
        return SolicitudAmistadDTO.builder()
                .idReceptor(solicitudAmistad.getIdReceptor())
                .estadoSolicitud(solicitudAmistad.getEstadoSolicitud())
                .build();
    }
}
