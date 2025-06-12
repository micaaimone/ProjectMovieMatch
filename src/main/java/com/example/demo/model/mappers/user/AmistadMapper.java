package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.user.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.user.SolicitudAmistadDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.User.AmistadEntity;
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
