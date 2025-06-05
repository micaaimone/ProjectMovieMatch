package com.example.demo.model.services;

import com.example.demo.model.DTOs.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.SolicitudAmistadDTO;
import com.example.demo.model.entities.SolicitudAmistadEntity;
import com.example.demo.model.enums.EstadoSolicitud;
import com.example.demo.model.exceptions.SolicitudAlreadyExistsException;
import com.example.demo.model.exceptions.SolicitudNotFound;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.SolicitudAmistadMapper;
import com.example.demo.model.repositories.SolicitudAmistadRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SolicitudAmistadService {

    private final SolicitudAmistadRepository solicitudAmistadRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudAmistadMapper solicitudAmistadMapper;

    public SolicitudAmistadService(SolicitudAmistadRepository solicitudAmistadRepository, UsuarioRepository usuarioRepository, SolicitudAmistadMapper solicitudAmistadMapper) {
        this.solicitudAmistadRepository = solicitudAmistadRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudAmistadMapper = solicitudAmistadMapper;
    }


    private void validarUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró el usuario con id: " + id));
    }


    public void save(NewSolicitudAmistadDTO solicitudAmistadDTO) {

        // validamos usuarios
        validarUsuario(solicitudAmistadDTO.getIdEmisor());
        validarUsuario(solicitudAmistadDTO.getIdReceptor());

        //verificamos q el id no sea el mismo
        if (solicitudAmistadDTO.getIdEmisor().equals(solicitudAmistadDTO.getIdReceptor())) {
            throw new IllegalArgumentException("No podés enviarte solicitud a vos mismo.");
        }

        //validamos q no este bloqueado, deberia hacerlo con query en repo?
        Optional<SolicitudAmistadEntity> solicitudExistente = solicitudAmistadRepository.findByIdEmisorAndIdReceptor(
                solicitudAmistadDTO.getIdReceptor(),
                solicitudAmistadDTO.getIdEmisor()
        );

        if (solicitudExistente.isPresent() &&
                solicitudExistente.get().getEstadoSolicitud() == EstadoSolicitud.BLOQUEADA) {
            throw new IllegalArgumentException("El usuario receptor ha bloqueado al emisor.");
        }

        // verificamos q no exista ya una solicitud con estos id's
        if (solicitudAmistadRepository.findByIdEmisorAndIdReceptor(solicitudAmistadDTO.getIdEmisor(), solicitudAmistadDTO.getIdReceptor()).isPresent()) {
            throw new SolicitudAlreadyExistsException("Ya existe una solicitud de amistad vigente.");
        }

        SolicitudAmistadEntity solicitudNueva = solicitudAmistadMapper.convertToNewEntity(solicitudAmistadDTO);
        solicitudNueva.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
        solicitudAmistadRepository.save(solicitudNueva);
    }

    public void aceptarSolicitud(Long idEmisor, Long idReceptor)
    {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        Optional<SolicitudAmistadEntity> solicitud = solicitudAmistadRepository.findByIdEmisorAndIdReceptor(
                idEmisor, idReceptor);

        SolicitudAmistadEntity solicitudAmistad = solicitud.orElseThrow(
                () -> new SolicitudNotFound("No se encontró una solicitud con los ID's enviados"));

        if (solicitudAmistad.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }



        solicitudAmistad.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);

        solicitudAmistadRepository.save(solicitudAmistad);
    }

    public void rechazarSolicitud(Long idEmisor, Long idReceptor)
    {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        Optional<SolicitudAmistadEntity> solicitudNueva = solicitudAmistadRepository.findByIdEmisorAndIdReceptor(
                idEmisor, idReceptor);

        SolicitudAmistadEntity solicitudAmistad = solicitudNueva.orElseThrow(
                () -> new SolicitudNotFound("No se encontró una solicitud con los ID's enviados"));

        if (solicitudAmistad.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }

        solicitudAmistad.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);

        solicitudAmistadRepository.save(solicitudAmistad);
    }

    public void bloquearSolicitud(Long idEmisor, Long idReceptor)
    {
        //validamos usuarios
        validarUsuario(idEmisor);
        validarUsuario(idReceptor);

        //validamos la existencia de esa solicitud
        Optional<SolicitudAmistadEntity> solicitudNueva = solicitudAmistadRepository.findByIdEmisorAndIdReceptor(
                idEmisor, idReceptor);

        SolicitudAmistadEntity solicitudAmistad = solicitudNueva.orElseThrow(
                () -> new SolicitudNotFound("No se encontró una solicitud con los ID's enviados"));

        if (solicitudAmistad.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden aceptar solicitudes pendientes.");
        }

        solicitudAmistad.setEstadoSolicitud(EstadoSolicitud.BLOQUEADA);

        solicitudAmistadRepository.save(solicitudAmistad);
    }

    public Page<SolicitudAmistadDTO> listarMisSolicitudes(Long idEmisor, Pageable pageable)
    {
        //validamos usuarios
        validarUsuario(idEmisor);

        Page<SolicitudAmistadEntity> page = solicitudAmistadRepository.findAllByIdEmisor(idEmisor, pageable);


        if (page.getContent().isEmpty()) {
            throw new SolicitudNotFound("No se encontraron solicitudes de este usuario.");
        }

        return page.map(solicitudAmistadMapper::convertToDTO);
    }


//    public void modificar(SolicitudAmistadDTO solicitudAmistadDTO)
//    {
//        //validamos usuarios
//        validarUsuario(solicitudAmistadDTO.getId_emisor());
//        validarUsuario(solicitudAmistadDTO.getId_receptor());
//
//
//        //validamos la existencia de esa solicitud
//        Optional<SolicitudAmistadEntity> solicitudNueva = solicitudAmistadRepository.findByIDs(
//                solicitudAmistadDTO.getId_emisor(),
//                solicitudAmistadDTO.getId_receptor()
//        );
//
//        SolicitudAmistadEntity solicitudAmistad = solicitudNueva.orElseThrow(
//                () -> new SolicitudNotFound("No se encontró una solicitud con los ID's enviados")
//        );
//
//        solicitudAmistad.setEstadoSolicitud(solicitudAmistadDTO.getEstadoSolicitud());
//
//
//        solicitudAmistadRepository.save(solicitudAmistad);
//
//
//    }
}
