package com.example.demo.model.mappers;

import com.example.demo.model.DTOs.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.SolicitudAmistadDTO;
import com.example.demo.model.entities.AmistadEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AmistadMapper {

    private final ModelMapper modelMapper;

    public AmistadMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AmistadEntity convertToNewEntity(NewSolicitudAmistadDTO newSolicitudAmistadDTO)
    {
        return AmistadEntity.builder()
                .idEmisor(newSolicitudAmistadDTO.getIdEmisor())
                .idReceptor(newSolicitudAmistadDTO.getIdReceptor())
                .build();
    }

    public SolicitudAmistadDTO convertToDTO(AmistadEntity solicitudAmistad)
    {
        return SolicitudAmistadDTO.builder()
                .idReceptor(solicitudAmistad.getIdReceptor())
                .estadoSolicitud(solicitudAmistad.getEstadoSolicitud())
                .build();
    }
}
