package com.example.demo.model.mappers.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReseniaMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ReseniaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        configureMappings();
    }

    /*
    * Un TypeMap es una configuración o “mapa” que le dice a ModelMapper cómo transformar (mapear) objetos de un tipo específico a otro tipo específico.

Tipo origen: la clase de donde querés copiar los datos (ejemplo, ReseniaEntity).

Tipo destino: la clase a la que querés transformar esos datos (ejemplo, ReseniaDTO)
* */

    private void configureMappings() {
        modelMapper.createTypeMap(ReseniaEntity.class, ReseniaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUsuario().getId(), ReseniaDTO::setId_usuario);
                    mapper.map(src -> src.getContenido().getId_contenido(), ReseniaDTO::setId_contenido);
                    mapper.map(ReseniaEntity::getPuntuacionU, ReseniaDTO::setPuntuacionU);
                    mapper.map(ReseniaEntity::getComentario, ReseniaDTO::setComentario);
                    mapper.map(src -> src.getUsuario().getUsername(), ReseniaDTO::setUsername);
                    mapper.map(src -> src.getContenido().getTitulo(), ReseniaDTO::setTitulo);
                });

        modelMapper.createTypeMap(ReseniaEntity.class, ReseniaMostrarUsuarioDTO.class)
                .addMappings(mapper -> {
                    mapper.map(ReseniaEntity::getId_resenia, ReseniaMostrarUsuarioDTO::setId);
                    mapper.map(src -> src.getUsuario().getNombre(), ReseniaMostrarUsuarioDTO::setNombre);
                    mapper.map(ReseniaEntity::getPuntuacionU, ReseniaMostrarUsuarioDTO::setPuntuacionU);
                    mapper.map(ReseniaEntity::getComentario, ReseniaMostrarUsuarioDTO::setComentario);
                });
    }


    public ReseniaMostrarUsuarioDTO convertToDTOUsuario(ReseniaEntity reseniaEntity) {
        return modelMapper.map(reseniaEntity, ReseniaMostrarUsuarioDTO.class);
    }

    public ReseniaDTO convertToDTO(ReseniaEntity reseniaEntity) {
        return modelMapper.map(reseniaEntity, ReseniaDTO.class);
    }

    public ReseniaEntity convertToEntity(ReseniaDTO reseniaDTO, UsuarioEntity usuario, ContenidoEntity contenido) {
        ReseniaEntity entity = modelMapper.map(reseniaDTO, ReseniaEntity.class);
        entity.setUsuario(usuario);
        entity.setContenido(contenido);
        entity.setFecha(LocalDateTime.now());
        return entity;
    }
}
