package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.ReseniaDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReseniaMapper {
    @Autowired
    private ModelMapper modelMapper;
    private UsuarioRepository usuarioRepository;
    private ContenidoRepository contenidoRepository;

    public ReseniaDTO convertToDTO(ReseniaEntity reseñaEntity)
    {
        return ReseniaDTO.builder()
                .id(reseñaEntity.getId_resenia())
                .id_usuario(reseñaEntity.getUsuario().getId())
                .id_contenido(reseñaEntity.getContenido().getId_contenido())
                .puntuacionU(reseñaEntity.getPuntuacionU())
                .comentario(reseñaEntity.getComentario())
                .build();
    }

    public ReseniaEntity convertToEntity(ReseniaDTO reseñaDTO, UsuarioEntity usuario, ContenidoEntity contenido)
    {
        return ReseniaEntity.builder()
                .id_resenia(reseñaDTO.getId())
                .usuario(usuario)
                .contenido(contenido)
                .puntuacionU(reseñaDTO.getPuntuacionU())
                .comentario(reseñaDTO.getComentario())
                .fecha((LocalDateTime.now()))
                .build();
    }
}
