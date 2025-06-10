package com.example.demo.model.services.Usuarios;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.Seguridad.repositories.RoleRepository;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;


    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    public Page<UsuarioDTO> findAll(Pageable pageable){
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }

    public void save(NewUsuarioDTO usuarioDTO) {

        if(usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new UsuarioYaExisteException("No se encontro un usuario con el username especificado");
        }

        UsuarioEntity usuario = usuarioMapper.convertToNewEntity(usuarioDTO);

        // Creamos la credencial asociada
        CredentialsEntity credencial = new CredentialsEntity();
        credencial.setEmail(usuarioDTO.getEmail());
        credencial.setPassword(passwordEncoder.encode(usuarioDTO.getContrasenia()));
        credencial.setUsuario(usuario); // relación bidireccional
        // Obtenemos el rol desde base
        RoleEntity roleUser = roleRepository.findByRole(Role.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No se encontró el rol USER"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);
        credencial.setRoles(roles);

        // Asignamos la credencial al usuario
        usuario.setCredencial(credencial);

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


        if (nuevosDatos.getTelefono() != null) {
            existente.setTelefono(nuevosDatos.getTelefono());
        }

        if (nuevosDatos.getUsername() != null) {
            existente.setUsername(nuevosDatos.getUsername());
        }


        usuarioRepository.save(existente);
    }

    public UsuarioEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();

        CredentialsEntity credencialAutenticada = credentialsRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Credencial no encontrada"));

        return credencialAutenticada.getUsuario();
    }

    public void cambiarEstadoUsuario(Long id, boolean activo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsuarioEntity usuarioAutenticado = getUsuarioAutenticado();

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Si no es admin y el id no coincide, le tiro excepción
        if (!esAdmin && !usuarioAutenticado.getId().equals(id)) {
            throw new AccessDeniedException("No tenés permiso para desactivar este usuario.");
        }

        // Busco al usuario que quiero modificar
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

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


    // agregar findById, por eso está comentado
    public void darLike(Long idUsuario, Long idContenido){
        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el id: " + idUsuario));
        ContenidoEntity contenidoEntity = contenidoRepository.findById(idContenido)
                .orElseThrow(()->new ContenidoNotFound("Contenido no encontrado con el id: " + idContenido));

        usuarioEntity.getLikes().add(contenidoEntity);
        usuarioRepository.save(usuarioEntity);
    }

    public void quitarLike(Long idUsuario, Long idContenido){
        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario con el id: " + idUsuario));
        ContenidoEntity contenidoEntity = contenidoRepository.findById(idContenido)
                .orElseThrow(()->new ContenidoNotFound("Contenido no encontrado con el id: " + idContenido));

        usuarioEntity.getLikes().remove(contenidoEntity);
        usuarioRepository.save(usuarioEntity);
    }

    //despues cambiar por contenidoDTO y retornar page
//    public Set<ContenidoEntity> listarLikes(Long id){
//        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        return usuarioEntity.getLikes();
//    }

    public Page<ContenidoEntity> obtenerLikes(Long id, Pageable pageable) {
        return usuarioRepository.findLikes(id, pageable);
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
