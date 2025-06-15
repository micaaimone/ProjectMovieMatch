package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReseniaMapper {
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;

    @Autowired
    public ReseniaMapper(ModelMapper modelMapper, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository) {
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
    }

    public ReseniaDTO convertToDTO(ReseniaEntity reseñaEntity)
    {
        return ReseniaDTO.builder()
                .id_usuario(reseñaEntity.getUsuario().getId())
                .id_contenido(reseñaEntity.getContenido().getId_contenido())
                .puntuacionU(reseñaEntity.getPuntuacionU())
                .comentario(reseñaEntity.getComentario())
                .build();
    }

    public ReseniaMostrarUsuarioDTO convertToDTOUsuario(ReseniaEntity reseñaEntity)
    {
        return ReseniaMostrarUsuarioDTO.builder()
                .id(reseñaEntity.getId_resenia())
                .nombre(reseñaEntity.getContenido().getTitulo())
                .puntuacionU(reseñaEntity.getPuntuacionU())
                .comentario(reseñaEntity.getComentario())
                .build();
    }

    public ReseniaEntity convertToEntity(ReseniaDTO reseñaDTO, UsuarioEntity usuario, ContenidoEntity contenido)
    {
        return ReseniaEntity.builder()
                .usuario(usuario)
                .contenido(contenido)
                .puntuacionU(reseñaDTO.getPuntuacionU())
                .comentario(reseñaDTO.getComentario())
                .fecha((LocalDateTime.now()))
                .build();
    }
}
