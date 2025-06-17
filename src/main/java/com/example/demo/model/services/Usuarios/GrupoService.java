package com.example.demo.model.services.Usuarios;

import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.user.Grupo.ModificarGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.GrupoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.AmistadExceptions.UsuariosNoSonAmigos;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEsAdminException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.user.GrupoMapper;
import com.example.demo.model.repositories.Usuarios.GrupoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GrupoService {
    private final GrupoMapper grupoMapper;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoMapper contenidoMapper;

    @Autowired
    public GrupoService(GrupoMapper grupoMapper, GrupoRepository grupoRepository, UsuarioRepository usuarioRepository, ContenidoMapper contenidoMapper) {
        this.grupoMapper = grupoMapper;
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
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

        usuariosValidos.add(admin);

        // crear grupo manualmente
        GrupoEntity grupo = new GrupoEntity();
        grupo.setNombre(grupoDTO.getNombre());
        grupo.setDescripcion(grupoDTO.getDescripcion());
        grupo.setAdministrador(admin);
        grupo.setListaUsuarios(usuariosValidos);

        // guardar
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
        if(grupo.getAdministrador().getId().equals(idAdmin)
                || esPremium(usuario))
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
        if(grupo.getAdministrador().getId().equals(idAdmin)
                || esPremium(usuario))

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
        UsuarioEntity usuario = userExiste(idAdmin);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q este en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idAdmin + " no se encuentra en el grupo");
        }

        //validamos q el q sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin)
                || esPremium(usuario))
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
        UsuarioEntity usuario = userExiste(idAdmin);

        //validamos grupo
        GrupoEntity grupo = grupoExiste(idGrupo);

        //validamos q este en el grupo
        if (!usuarioPerteneceAGrupo(grupo, idAdmin)) {
            throw new UsuarioNoEncontradoException("El usuario con ID " + idAdmin + " no se encuentra en el grupo");
        }

        //validamos q el q sea realmente admin
        if(grupo.getAdministrador().getId().equals(idAdmin)
                || esPremium(usuario))
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

    public Page<ContenidoMostrarDTO> mostrarCoincidencias(Long idGrupo, int page, int size) {
        GrupoEntity grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        Set<UsuarioEntity> usuarios = grupo.getListaUsuarios();

        // Debug: mostrar cantidad usuarios y sus likes
        System.out.println("Usuarios en grupo: " + usuarios.size());
        usuarios.forEach(u -> System.out.println("Usuario: " + u.getNombre() + ", likes: " + u.getContenidoLikes().size()));


        // Contar likes por contneido
        Map<ContenidoEntity, Integer> conteoLikes = usuarios.stream()
                .flatMap(usuario -> usuario.getContenidoLikes().stream()) // o getLikes() pero devuelve entities intermedias
                .map(ContenidoLikeEntity::getContenido)
                .collect(Collectors.toMap(
                        contenido -> contenido,
                        contenido -> 1,
                        Integer::sum
                ));

        // Debug: mostrar conteo de likes por contenido
        conteoLikes.forEach((contenido, count) -> System.out.println("Contenido: " + contenido.getTitulo() + ", Likes: " + count));

        // Filtrar pelis con al menos 3 likes y ordenar desc
        List<ContenidoEntity> peliculasConMatch = conteoLikes.entrySet().stream()
                .filter(entry -> entry.getValue() >= 1)
                .sorted(Map.Entry.<ContenidoEntity, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        // Verificar si hay algún usuario premium
        boolean algunoPremium = usuarios.stream().anyMatch(this::esPremium);
        int limite = algunoPremium ? 10 : 3;

        // Aplicar límite máximo a la lista total de coincidencias
        List<ContenidoEntity> peliculasLimitadas = peliculasConMatch.stream()
                .limit(limite)
                .collect(Collectors.toList());

        // Paginación manual sobre lista limitada
        int start = page * size;
        int end = Math.min(start + size, peliculasLimitadas.size());
        List<ContenidoEntity> paginaPeliculas = start < end ? peliculasLimitadas.subList(start, end) : Collections.emptyList();

        // Mapear a DTO
        List<ContenidoMostrarDTO> dtos = paginaPeliculas.stream()
                .map(contenidoMapper::convertToDTOForAdmin)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, PageRequest.of(page, size), peliculasLimitadas.size());
    }

    private boolean esPremium(UsuarioEntity usuario) {
        return usuario.getCredencial().getRoles().stream()
                .anyMatch(role -> role.equals(Role.ROLE_PREMIUM));
    }


}