package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.user.UsuarioMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.Specifications.UsuarioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;


@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;


    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
    }

    public Page<UsuarioDTO> findAll(Pageable pageable){
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }

    public void save(NewUsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el email especificado");
        }

        if(usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el username especificado");
        }


        UsuarioEntity usuario = usuarioMapper.convertToNewEntity(usuarioDTO);

        usuarioRepository.save(usuario);

    }

    public UsuarioDTO findById(long id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el id: " + id));
    }

    public UsuarioDTO findByUsername(String username){
        return usuarioRepository.findByUsername(username)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encuentra un usuario con el username: " + username));
    }


    public void actualizarUsuario(Long id, UsuarioModificarDTO nuevosDatos) {
        UsuarioEntity existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el username: " + nuevosDatos.getUsername() ));

        if (nuevosDatos.getEmail() != null) {
            existente.setEmail(nuevosDatos.getEmail());
        }

        if (nuevosDatos.getTelefono() != null) {
            existente.setTelefono(nuevosDatos.getTelefono());
        }

        if (nuevosDatos.getUsername() != null) {
            existente.setUsername(nuevosDatos.getUsername());
        }

        if (nuevosDatos.getContrasenia() != null) {
            existente.setContrasenia(nuevosDatos.getContrasenia());
        }

        if (nuevosDatos.getGeneros() != null) {
            existente.setGeneros(nuevosDatos.getGeneros());
        }

        usuarioRepository.save(existente);
    }


    //se puede ahorrar mandar el boolean
    public void cambiarEstadoUsuario(Long id, boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el id: " + id));

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
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el id: " + id));

    }

    public Page<UsuarioDTO> obtenerUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }


    public Page<UsuarioDTO> buscarUsuarios(String nombre, String apellido, String email, String username, Boolean activo, Pageable pageable){
        Specification<UsuarioEntity> spec = Specification
                .where(UsuarioSpecification.nombre(nombre))
                .and(UsuarioSpecification.apellido(apellido))
                .and(UsuarioSpecification.email(email))
                .and(UsuarioSpecification.username(username))
                .and(UsuarioSpecification.activo(activo));

        Page<UsuarioEntity> page = usuarioRepository.findAll(spec, pageable);


        if (page.getContent().isEmpty()) {
            throw new UsuarioNoEncontradoException("No se encontraron contenidos con los filtros especificados.");
        }

        return page.map(usuarioMapper::convertToDTO);
    }


}
