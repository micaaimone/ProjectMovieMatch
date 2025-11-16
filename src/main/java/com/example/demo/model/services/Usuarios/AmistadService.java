package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.Amistad.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.Amistad.SolicitudAmistadDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.AmistadEntity;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.enums.EstadoSolicitud;
import com.example.demo.model.exceptions.AmistadExceptions.SolicitudAlreadyExistsException;
import com.example.demo.model.exceptions.AmistadExceptions.SolicitudNotFound;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.user.AmistadMapper;
import com.example.demo.model.repositories.Usuarios.AmistadRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AmistadService {

    private final AmistadRepository solicitudAmistadRepository;
    private final UsuarioRepository usuarioRepository;
    private final AmistadMapper solicitudAmistadMapper;
    private final ContenidoMapper contenidoMapper;
    private final AmistadMapper amistadMapper;


    public AmistadService(AmistadRepository solicitudAmistadRepository, UsuarioRepository usuarioRepository, AmistadMapper solicitudAmistadMapper, ContenidoMapper contenidoMapper, AmistadMapper amistadMapper) {
        this.solicitudAmistadRepository = solicitudAmistadRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudAmistadMapper = solicitudAmistadMapper;
        this.contenidoMapper = contenidoMapper;
        this.amistadMapper = amistadMapper;
    }

    private UsuarioEntity validarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un usuario con el id: " + id));

        if (!usuario.getActivo()) {
            throw new UsuarioNoEncontradoException("El usuario con id " + id + " no está activo.");
        }

        return usuario;
    }



    public AmistadEntity validarSolicitud(Long idEmisor, Long idReceptor) {
        AmistadEntity solicitud = solicitudAmistadRepository
                .findByIdEmisorAndIdReceptor(idEmisor, idReceptor)
                .orElseThrow(() -> new SolicitudNotFound("No se encontró una solicitud con los ID's enviados"));

        if (solicitud.getEstadoSolicitud() == EstadoSolicitud.BLOQUEADA) {
            throw new IllegalArgumentException("El usuario receptor ha bloqueado al emisor.");
        }

        return solicitud;
    }

    public void save(NewSolicitudAmistadDTO solicitudAmistadDTO, Long idEmisor) {

        Long idReceptor = solicitudAmistadDTO.getIdReceptor();

        // validamos usuarios
        UsuarioEntity emisor = validarUsuario(idEmisor);
        UsuarioEntity receptor = validarUsuario(idReceptor);

        // validamos q el usuario no mande su id por equivocacion
        if (idEmisor.equals(idReceptor)) {
            throw new IllegalArgumentException("No podés enviarte solicitud a vos mismo.");
        }

        // validamos que no sean ya amigos
        if (emisor.getAmigos().contains(receptor) || receptor.getAmigos().contains(emisor)) {
            throw new SolicitudAlreadyExistsException("Ya son amigos.");
        }

        // verificamos si ya existe una solicitud en cualquier sentido
        Optional<AmistadEntity> solicitudExistenteDirecta =
                solicitudAmistadRepository.findByIdEmisorAndIdReceptor(idEmisor, idReceptor);

        Optional<AmistadEntity> solicitudExistenteInversa =
                solicitudAmistadRepository.findByIdEmisorAndIdReceptor(idReceptor, idEmisor);

        if (solicitudExistenteDirecta.isPresent()) {
            throw new SolicitudAlreadyExistsException("Ya enviaste una solicitud a este usuario.");
        }

        // si existe una solicitud inversa, evaluamos su estado
        if (solicitudExistenteInversa.isPresent()) {
            EstadoSolicitud estado = solicitudExistenteInversa.get().getEstadoSolicitud();
            if (estado == EstadoSolicitud.PENDIENTE) {
                throw new SolicitudAlreadyExistsException("Este usuario ya te envió una solicitud pendiente.");
            }
            if (estado == EstadoSolicitud.BLOQUEADA) {
                throw new IllegalArgumentException("No podés enviar solicitud: el usuario receptor te bloqueó.");
            }
        }

        AmistadEntity solicitudNueva = solicitudAmistadMapper.convertToNewEntity(solicitudAmistadDTO);
        solicitudNueva.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitudNueva.setIdEmisor(idEmisor);

        solicitudAmistadRepository.save(solicitudNueva);
    }

    public void aceptarSolicitud(Long idEmisor, Long idReceptor) {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        AmistadEntity solicitud = validarSolicitud(idEmisor, idReceptor);

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }

        solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);

        solicitudAmistadRepository.save(solicitud);

        // agregar amigos mutuos
        UsuarioEntity emisor = usuarioRepository.findById(idEmisor).get();
        UsuarioEntity receptor = usuarioRepository.findById(idReceptor).get();

        emisor.getAmigos().add(receptor);
        receptor.getAmigos().add(emisor);

        usuarioRepository.save(emisor);
        usuarioRepository.save(receptor);
    }

    public void rechazarSolicitud(Long idEmisor, Long idReceptor) {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        AmistadEntity solicitud = validarSolicitud(idEmisor, idReceptor);

        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }

        solicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);

        solicitudAmistadRepository.save(solicitud);

    }

    public void bloquearSolicitud(Long idEmisor, Long idReceptor) {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        AmistadEntity solicitud = validarSolicitud(idEmisor, idReceptor);


        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }

        solicitud.setEstadoSolicitud(EstadoSolicitud.BLOQUEADA);

        solicitudAmistadRepository.save(solicitud);
    }

    public Page<SolicitudAmistadDTO> listarMisSolicitudes(Long idEmisor, Pageable pageable) {
        //validamos usuarios
        validarUsuario(idEmisor);

        Page<AmistadEntity> page = solicitudAmistadRepository
                .findAllByIdEmisor(idEmisor, pageable);


        if (page.getContent().isEmpty()) {
            throw new SolicitudNotFound("No se encontraron solicitudes de este usuario.");
        }

        return page.map(solicitudAmistadMapper::convertToDTO);
    }

    public SolicitudAmistadDTO obtenerSolicitud(Long idEmisor, Long idReceptor) {
        // validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        // buscamos solicitud -- uso validar solicitud porque devuelve la solicitud
        AmistadEntity solicitud = validarSolicitud(idEmisor, idReceptor);

        return solicitudAmistadMapper.convertToDTO(solicitud);
    }

    public void cancelarSolicitud(Long idEmisor, Long idReceptor) {
        // validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        // buscamos solicitud-- uso validar solicitud porque devuelve la solicitud
        AmistadEntity solicitud = validarSolicitud(idEmisor, idReceptor);

        // solo se puede cancelar si está pendiente
        if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden cancelar solicitudes pendientes.");
        }

        solicitudAmistadRepository.delete(solicitud);
    }

    public Page<ContenidoMostrarDTO> visualizarCoincidencias(Long idEmisor, Long idReceptor, Pageable pageable) {
        UsuarioEntity emisor = validarUsuario(idEmisor);
        UsuarioEntity receptor = validarUsuario(idReceptor);

        // Sacamos los ContenidoEntity de los likes de cada usuario
        List<ContenidoEntity> likesEmisor = emisor.getContenidoLikes()
                .stream()
                .map(ContenidoLikeEntity::getContenido)
                .toList();

        List<ContenidoEntity> likesReceptor = receptor.getContenidoLikes()
                .stream()
                .map(ContenidoLikeEntity::getContenido)
                .toList();

        // Buscamos las coincidencias entre los dos sets de contenidos
        List<ContenidoMostrarDTO> coincidencias = likesEmisor.stream()
                .filter(likesReceptor::contains)
                .map(contenidoMapper::convertToDTOForAdmin)
                .limit(3)
                .toList();

        if (coincidencias.isEmpty()) {
            throw new SolicitudNotFound("No se encontraron coincidencias entre los usuarios.");
        }

        return new PageImpl<>(coincidencias, pageable, coincidencias.size());
    }

    public Page<SolicitudAmistadDTO> listarSolicitudesPendientes(Long idReceptor, Pageable pageable) {
        // validamos usuario
        validarUsuario(idReceptor);

        Page<AmistadEntity> solicitudes = solicitudAmistadRepository
                .findAllByIdReceptorAndEstadoSolicitud(idReceptor, EstadoSolicitud.PENDIENTE, pageable);

        if (solicitudes.getContent().isEmpty()) {
            throw new SolicitudNotFound("No se encontraron solicitudes pendientes para este usuario.");
        }

        return solicitudes.map(solicitudAmistadMapper::convertToDTO);
    }

    public void eliminarAmigo(Long idEmisor, Long idReceptor) {
        // validamos usuarios
        UsuarioEntity emisor = validarUsuario(idEmisor);
        UsuarioEntity receptor = validarUsuario(idReceptor);

        // validamos q el usuario no mande su id por equivocacion
        if (idEmisor.equals(idReceptor)) {
            throw new IllegalArgumentException("No podés eliminarte de tus propias amistades.");
        }

        // validamos que no se hayan eliminado ya
        if (!emisor.getAmigos().contains(receptor) || !receptor.getAmigos().contains(emisor)) {
            throw new SolicitudNotFound("No son amigos.");
        }

        // elimino amigo del set de amigos de usuario
        boolean eliminadoDeUsuario = emisor.getAmigos().remove(receptor);
        // Remover usuario del set de amigos de amigo (relación bidireccional)
        boolean eliminadoDeAmigo = receptor.getAmigos().remove(emisor);

        if (eliminadoDeUsuario && eliminadoDeAmigo) {
            usuarioRepository.save(emisor);
            usuarioRepository.save(receptor);

            // ---- BORRAR LA AMISTAD EN LA TABLA ----
            solicitudAmistadRepository.deleteAmistadBidireccional(idEmisor, idReceptor);
        } else {
            throw new RuntimeException("No existe la relación de amistad entre los usuarios.");
        }
    }

    public Page<AmigoDTO> mostrarMisAmigos(Long idEmisor, Pageable pageable) {
        // validamos usuario
        UsuarioEntity emisor = validarUsuario(idEmisor);

        List<UsuarioEntity> amigos = emisor.getAmigos();

        List<AmigoDTO> amigosDTOs = amigos.stream()
                .map(amistadMapper::convertToAmigoDTO)
                .toList();

        return new PageImpl<>(amigosDTOs, pageable, amigosDTOs.size());

    }


}
