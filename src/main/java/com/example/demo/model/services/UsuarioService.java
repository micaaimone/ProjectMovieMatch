package com.example.demo.model.services;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UsuarioService {
    //private UsuarioMapper usuarioMapper;

    // cambiarlo al constructor
    @Autowired
    private UsuarioRepository usuarioRepository;
    private ContenidoService contenidoService;

    public List<UsuarioEntity> findAll(){
        return usuarioRepository.findAll();
    }

    public void save(UsuarioEntity u){
        usuarioRepository.save(u);
    }

    public Optional<UsuarioEntity> findById(long id){
        return usuarioRepository.findById(id);
    }

    public UsuarioEntity findByUsername(String username){return usuarioRepository.findByUsername(username);}

    public void deleteById(long id){
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioEntity> usuariosMayores(int edad){
        return usuarioRepository.findByEdadGreaterThan(edad);
    }

    // crear el DTO model
//    public UsuarioDTO getUsuarioDTO(Long id){
//        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow();
//        return usuarioMapper.toDTO(usuarioEntity);
//    }
//
//    public List<UsuarioDTO> getAllDTO(){
//        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
//
//        return usuarios.stream()
//                .map(usuarioMapper::toDTO)
//                .collect(Collectors.toList());
//    }

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
