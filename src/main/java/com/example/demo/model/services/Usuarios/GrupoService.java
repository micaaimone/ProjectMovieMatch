package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.user.Grupo.ModificarGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.GrupoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.AmistadExceptions.UsuariosNoSonAmigos;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEsAdminException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.user.GrupoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.GrupoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GrupoService {
    private final GrupoMapper grupoMapper;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;
    private final ContenidoMapper contenidoMapper;

    @Autowired
    public GrupoService(GrupoMapper grupoMapper, GrupoRepository grupoRepository, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ContenidoMapper contenidoMapper) {
        this.grupoMapper = grupoMapper;
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.contenidoMapper = contenidoMapper;
    }

    public void save(NewGrupoDTO grupoDTO, Long idUsuario) {
        // buscamos al admin
        UsuarioEntity admin = userExiste(idUsuario);

        // validamos que todos los IDs existan y sean amigos del creador
        Set<UsuarioEntity> usuariosValidos = grupoDTO.getIdUsuarios().stream()
                .map(id -> usuarioRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario con ID " + id + " no existe")))
                .peek(usuario -> {
                    if (!admin.getAmigos().contains(usuario)) {
                        throw new UsuariosNoSonAmigos("El usuario con ID " + usuario.getId() + " no es amigo del creador");
                    }
                })
                .collect(Collectors.toSet());

        GrupoEntity grupo = grupoMapper.convertToEntity(grupoDTO);

        // seteo los usuarios validados
        grupo.setListaUsuarios(usuariosValidos);

        // seteo al admin
        grupo.setAdministrador(admin);

        grupoRepository.save(grupo);
    }

    public VisualizarGrupoDTO mostrarGrupoPorID(Long idGrupo, Long idUsuario) {
        //traigo al usuario q busca porque sino no puede visualizar el grupo

        // buscamos al user
        userExiste(idUsuario);

        GrupoEntity grupo = grupoExiste(idGrupo);

        // buscamos que el usuario pertenezca al grupo
        if (!usuarioPerteneceAGrupo(grupo, idUsuario)) {

            throw new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no se encuentra en el grupo");
        }

        return grupoMapper.convertToVisualizarGrupo(grupo);
    }

    public Page<VisualizarGrupoDTO> visualizarGrupoPorUsuario(Long idUsuario, int page, int size) {
        // buscamos al user
        userExiste(idUsuario);

        Pageable pageable = PageRequest.of(page, size);

        Page<GrupoEntity> grupos = grupoRepository.findAllByListaUsuarios_Id(idUsuario, pageable);

        return grupos.map(grupoMapper::convertToVisualizarGrupo);
    }

    public VisualizarGrupoDTO visualizarPorNombre(String nombre, Long idUsuario) {
        // buscamos al user
        userExiste(idUsuario);

        GrupoEntity grupo = grupoRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("El grupo con el nombre " + nombre + " no existe"));

        if (!usuarioPerteneceAGrupo(grupo, idUsuario)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no se encuentra en el grupo");
        }

        return grupoMapper.convertToVisualizarGrupo(grupo);
    }

    private UsuarioEntity userExiste(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no existe"));
    }

    private GrupoEntity grupoExiste(Long idGrupo) {
        return grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new EntityNotFoundException("El grupo con ID " + idGrupo + " no existe"));
    }

    private boolean usuarioPerteneceAGrupo(GrupoEntity grupo, Long idUsuario) {
        return grupo.getListaUsuarios().stream()
                .anyMatch(u -> u.getId().equals(idUsuario));
    }

    public void eliminarUsuarioDeGrupo(Long idGrupo, Long idAdmin, Long idUsuario){
        //validamos usuarios
        UsuarioEntity admin = userExiste(idAdmin);
        UsuarioEntity usuario = userExiste(idUsuario);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q ambos esten en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idUsuario)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no se encuentra en el grupo");
        }

        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no se encuentra en el grupo");
        }

        //validamos q el q quiere eliminar sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin))
        {
            grupo.getListaUsuarios().removeIf(u -> u.getId().equals(idUsuario));
        } else {
            //tengo q crear la exception
            throw new UsuarioNoEsAdminException("El usuario no es el administrador del gurpo");
        }

        // guardamos cambios
        grupoRepository.save(grupo);
    }

    public void agregarUsuarioAGrupo(Long idGrupo, Long idAdmin, Long idUsuario){
        //validamos usuarios
        UsuarioEntity admin = userExiste(idAdmin);
        UsuarioEntity usuario = userExiste(idUsuario);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q este en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idUsuario + " no se encuentra en el grupo");
        }

        //validamos q el q sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin))
        {
            // validamos que el usuario no esté ya en el grupo
            if (usuarioPerteneceAGrupo(grupo, idUsuario)) {
                throw new UsuarioYaExisteException("El usuario con ID " + idUsuario + " ya está en el grupo");
            }
            grupo.getListaUsuarios().add(usuario);
        } else {
            //tengo q crear la exception
            throw new UsuarioNoEsAdminException("El usuario no es el administrador del grupo");
        }

        grupoRepository.save(grupo);
    }

    public void eliminarGrupo(Long idAdmin, Long idGrupo){
        //validamos usuario
        UsuarioEntity admin = userExiste(idAdmin);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q este en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idAdmin + " no se encuentra en el grupo");
        }

        //validamos q el q sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin))
        {
            grupoRepository.delete(grupo);
        } else {
            //tengo q crear la exception
            throw new UsuarioNoEsAdminException("El usuario no es el administrador del gurpo");
        }

    }

    public void modificarGrupo(Long idAdmin, Long idGrupo, ModificarGrupoDTO modificarGrupoDTO){
        //validamos q el dto no venga vacio
        if (modificarGrupoDTO.getDescripcion() == null && modificarGrupoDTO.getNombre() == null) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }

        //validamos usuario
        UsuarioEntity admin = userExiste(idAdmin);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q este en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idAdmin + " no se encuentra en el grupo");
        }

        //validamos q el q sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin))
        {
            if (modificarGrupoDTO.getDescripcion()  != null) {
                grupo.setDescripcion(modificarGrupoDTO.getDescripcion());
            }
            if (modificarGrupoDTO.getNombre() != null) {
                grupo.setNombre(modificarGrupoDTO.getNombre());
            }
        } else {
            //tengo q crear la exception
            throw new UsuarioNoEsAdminException("El usuario no es el administrador del grupo");
        }

        grupoRepository.save(grupo);
    }

    public Page<ContenidoDTO> obtenerMatchDeGrupo(List<Long> idsUsuarios, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ContenidoEntity> contenidoDTOS = contenidoRepository.obtenerMatchDeGrupo(idsUsuarios, pageable);

        return contenidoDTOS.map(contenidoMapper::convertToDTO);
    }

}
