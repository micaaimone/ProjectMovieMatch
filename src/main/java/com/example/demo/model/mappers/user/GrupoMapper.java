package com.example.demo.model.mappers.user;

import com.example.demo.model.DTOs.user.Grupo.ModificarGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.User.GrupoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GrupoMapper {
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public GrupoMapper(ModelMapper modelMapper, UsuarioRepository usuarioRepository) {
        this.modelMapper = modelMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public GrupoEntity convertToEntity(NewGrupoDTO newGrupoDTO) {
        GrupoEntity grupo = new GrupoEntity();
        grupo.setNombre(newGrupoDTO.getNombre());
        grupo.setDescripcion(newGrupoDTO.getDescripcion());

        Set<UsuarioEntity> usuarios = newGrupoDTO.getIdUsuarios().stream()
                .map(id -> usuarioRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id)))
                .collect(Collectors.toSet());

        grupo.setListaUsuarios(usuarios);
        return grupo;
    }
    public VisualizarGrupoDTO convertToVisualizarGrupo(GrupoEntity grupoEntity) {
        return VisualizarGrupoDTO.builder()
                .nombre(grupoEntity.getNombre())
                .descripcion(grupoEntity.getDescripcion()) // si es null, queda null, no hace falta if
                .build();
    }



}
