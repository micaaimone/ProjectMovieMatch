package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaMostrarUsuarioDTO;
import com.example.demo.model.DTOs.user.AmigoDTO;
import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.User.ContenidoLike;
import com.example.demo.model.entities.User.ReseniaLike;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.Contenido.ReseniaMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper{
    private final ModelMapper modelMapper;
    private final ReseniaMapper reseñaMapper;
    private final ContenidoMapper contenidoMapper;
    private final ListasMapper listasMapper;


    public UsuarioMapper(ModelMapper modelMapper, ReseniaMapper reseñaMapper, ContenidoMapper contenidoMapper, ListasMapper listasMapper) {
        this.modelMapper = modelMapper;
        this.reseñaMapper = reseñaMapper;
        this.contenidoMapper = contenidoMapper;
        this.listasMapper = listasMapper;
    }

    public AmigoDTO convertAmigoToDTO(UsuarioEntity usuarioEntity)
    {

        return modelMapper.map(usuarioEntity, AmigoDTO.class);
    }

    public UsuarioDTO convertToDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO dto = modelMapper.map(usuarioEntity, UsuarioDTO.class);

        if(usuarioEntity.getContenidoLikes() != null)
        {
            List<ContenidoEntity> contenidos = usuarioEntity.getContenidoLikes()
                    .stream()
                    .map(ContenidoLike::getContenido)
                    .toList();

            List<ContenidoMostrarDTO> contenidoDTOS = contenidos
                    .stream()
                    .map(contenidoMapper::convertToDTOForAdmin)
                    .toList();
            dto.setContenidoLikes(contenidoDTOS);
        }

        // revisar si está bien hechop
        if(usuarioEntity.getReseniaLikes() != null)
        {
            List<ReseniaEntity> resenias = usuarioEntity.getReseniaLikes()
                    .stream()
                    .map(ReseniaLike::getResenia)
                    .toList();

            List<ReseniaMostrarUsuarioDTO> reseniaDTOS = resenias
                    .stream()
                    .map(reseñaMapper::convertToDTOUsuario)
                    .toList();
            dto.setReseniaLikes(reseniaDTOS);
        }

        if(usuarioEntity.getAmigos() != null)
        {
            List<AmigoDTO> amigosDTOs = usuarioEntity.getAmigos()
                    .stream()
                    .map(this::convertAmigoToDTO)
                    .toList();
            dto.setAmigos(amigosDTOs);
        }

        if (usuarioEntity.getReseñasHechas() != null) {
            //mapeo las reseñas, las hago dto
            List<ReseniaMostrarUsuarioDTO> reseñasDTO = usuarioEntity.getReseñasHechas()
                    .stream()
                    .map(reseñaMapper::convertToDTOUsuario)
                    .toList();
            dto.setReseñas(reseñasDTO);
        }

        if(usuarioEntity.getListas() != null){
            List<ListaContenidoDTO> listaDTO = usuarioEntity.getListas()
                    .stream()
                    .map(listasMapper::convertToDTO)
                    .toList();
            dto.setListas(listaDTO);
        }
        return dto;
    }

    public UsuarioEntity convertToNewEntity(NewUsuarioDTO usuarioDTO)
    {
        return UsuarioEntity.builder()
                .edad(usuarioDTO.getEdad())
                .email(usuarioDTO.getEmail())
                .apellido(usuarioDTO.getApellido())
                .nombre(usuarioDTO.getNombre())
                .telefono(usuarioDTO.getTelefono())
                .username(usuarioDTO.getUsername())
                .contrasenia(usuarioDTO.getContrasenia())
                .activo(true)
                .generos(usuarioDTO.getGeneros())
                .build();
    }


    public UsuarioEntity convertToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioEntity.class);
    }
}
