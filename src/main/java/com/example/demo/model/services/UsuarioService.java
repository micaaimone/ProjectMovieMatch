package com.example.demo.model.services;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.UsuarioMapper;
import com.example.demo.model.repositories.UsuarioRepository;
import com.example.demo.model.specifications.UsuarioSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    //private final ContenidoService contenidoService; verificar si hay q traer el service o el repo


    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository /*ContenidoService contenidoService*/) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        //this.contenidoService = contenidoService;
    }

    public Page<UsuarioDTO> findAll(Pageable pageable){
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }

    public void save(UsuarioEntity usuario) {
//        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
//            throw new UsuarioYaExisteException(usuario.getEmail());
//        }
        usuarioRepository.save(usuario);

    }

    public UsuarioDTO findById(long id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    public UsuarioDTO findByUsername(String username){
        return usuarioRepository.findByUsername(username)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(/*excepcion personalizada*/);
    }

    //no hay que borrar, hay q hacer baja logica (activo =false)
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException(id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioEntity actualizarUsuario(Long id, UsuarioEntity nuevosDatos) {
        UsuarioEntity existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        existente.setNombre(nuevosDatos.getNombre());
        existente.setEmail(nuevosDatos.getEmail());

        return usuarioRepository.save(existente);
    }

    //se puede ahorrar mandar el boolean
    public void cambiarEstadoUsuario(Long id, boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    public Page<UsuarioDTO> usuariosMayores(Long edad, Pageable pageable) {
        return usuarioRepository.findByEdadGreaterThan(edad, pageable)
                .map(usuarioMapper::convertToDTO);
    }

    public UsuarioDTO getUsuarioDTO(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }

    public Page<UsuarioDTO> obtenerUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }


    // agregar findById, por eso está comentado
//    public void darLike(Long idUsuario, Long idContenido){
//        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        ContenidoEntity contenidoEntity = contenidoService.findById(idContenido)
//                .orElseThrow(()->new RuntimeException("Contenido no encontrado"));
//
//        usuarioEntity.getLikes().add(contenidoEntity);
//        usuarioRepository.save(usuarioEntity);
//    }
//
//    public void quitarLike(Long idUsuario, Long idContenido){
//        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        // agregar find by id
//        ContenidoEntity contenidoEntity = contenidoService.findById(idContenido)
//                .orElseThrow(()->new RuntimeException("Contenido no encontrado"));
//
//        usuarioEntity.getLikes().remove(contenidoEntity);
//        usuarioRepository.save(usuarioEntity);
//    }

    //despues cambiar por contenidoDTO y retornar page
//    public Set<ContenidoEntity> listarLikes(Long id){
//        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        return usuarioEntity.getLikes();
//    }

    public Page<ContenidoEntity> obtenerLikes(Long id, Pageable pageable) {
        return usuarioRepository.findLikes(id, pageable);
    }


    public Page<UsuarioEntity> buscarUsuarios(String nombre, String apellido, String email, String username, Boolean activo, Pageable pageable){
        Specification<UsuarioEntity> spec = Specification
                .where(UsuarioSpecification.nombre(nombre))
                .and(UsuarioSpecification.apellido(apellido))
                .and(UsuarioSpecification.email(email))
                .and(UsuarioSpecification.username(username))
                .and(UsuarioSpecification.activo(activo));

        return usuarioRepository.findAll(spec, pageable);
    }


}
