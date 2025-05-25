package com.example.demo.model.services;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.UsuarioMapper;
import com.example.demo.model.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioService(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    private UsuarioRepository usuarioRepository;
    private ContenidoService contenidoService;

    public List<UsuarioEntity> findAll(){
        return usuarioRepository.findAll();
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new UsuarioYaExisteException(usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioEntity> findById(long id){
        return usuarioRepository.findById(id);
    }

    public UsuarioEntity findByUsername(String username){return usuarioRepository.findByUsername(username);}

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

    public void cambiarEstadoUsuario(Long id, boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    public List<UsuarioEntity> usuariosMayores(int edad){
        return usuarioRepository.findByEdadGreaterThan(edad);
    }


    public UsuarioDTO convertirAUsuarioDTO(UsuarioEntity u) {
        return new UsuarioDTO(
                u.getUsername(),
                u.getEmail(),
                u.getCredencial(),
                u.getLikes()
                // tmb suscripcion
        );
    }
    public UsuarioDTO getUsuarioDTO(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return convertirAUsuarioDTO(usuario);
    }

    public Page<UsuarioDTO> obtenerUsuariosPaginados(Pageable pageable) {
        Page<UsuarioEntity> page = usuarioRepository.findAll(pageable);
        return page.map(this::convertirAUsuarioDTO);
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

    public Set<ContenidoEntity> listarLikes(Long id){
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioEntity.getLikes();
    }
}
