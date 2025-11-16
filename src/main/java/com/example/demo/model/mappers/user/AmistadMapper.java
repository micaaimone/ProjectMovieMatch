package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.Amistad.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.Amistad.SolicitudAmistadDTO;
import com.example.demo.model.entities.User.AmistadEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmistadMapper {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AmistadMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AmistadEntity convertToNewEntity(NewSolicitudAmistadDTO newSolicitudAmistadDTO)
    {
        return AmistadEntity.builder()
                .idReceptor(newSolicitudAmistadDTO.getIdReceptor())
                .username(newSolicitudAmistadDTO.getUsername())
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

        // Buscamos el usuario receptor
        UsuarioEntity receptor = usuarioRepository.findById(solicitudAmistad.getIdReceptor())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));

        return SolicitudAmistadDTO.builder()
                .idReceptor(solicitudAmistad.getIdReceptor())
                .idEmisor(solicitudAmistad.getIdEmisor())
                .estadoSolicitud(solicitudAmistad.getEstadoSolicitud())
                .username(receptor.getUsername())
                .build();
    }
}
