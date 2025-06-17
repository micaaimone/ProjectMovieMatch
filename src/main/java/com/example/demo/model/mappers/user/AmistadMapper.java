package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.Amistad.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.Amistad.SolicitudAmistadDTO;
import com.example.demo.model.entities.User.AmistadEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmistadMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public AmistadMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AmistadEntity convertToNewEntity(NewSolicitudAmistadDTO newSolicitudAmistadDTO)
    {
        return AmistadEntity.builder()
                .idReceptor(newSolicitudAmistadDTO.getIdReceptor())
                .build();
    }

    public AmigoDTO convertToAmigoDTO(UsuarioEntity usuario)
    {
        return AmigoDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .build();
    }

    public SolicitudAmistadDTO convertToDTO(AmistadEntity solicitudAmistad)
    {
        return SolicitudAmistadDTO.builder()
                .idReceptor(solicitudAmistad.getIdReceptor())
                .idEmisor(solicitudAmistad.getIdEmisor())
                .estadoSolicitud(solicitudAmistad.getEstadoSolicitud())
                .build();
    }
}
