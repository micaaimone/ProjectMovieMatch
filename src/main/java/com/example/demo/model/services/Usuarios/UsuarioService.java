package com.example.demo.model.services.Usuarios;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.Seguridad.repositories.RoleRepository;
import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioYaExisteException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.user.UsuarioMapper;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.Specifications.UsuarioSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;
    private final ContenidoMapper contenidoMapper;

    public UsuarioService(UsuarioMapper usuarioMapper, UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository, ContenidoMapper contenidoMapper) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
        this.contenidoMapper = contenidoMapper;
    }

    public void validarUsuarioExistente(NewUsuarioDTO usuarioDTO)
    {
        Optional<CredentialsEntity> credencialOptional = credentialsRepository.findByEmail(usuarioDTO.getEmail());

        if (credencialOptional.isPresent()) {
            UsuarioEntity usuarioExistente = credencialOptional.get().getUsuario();

            if (!usuarioExistente.getActivo()) {
                usuarioExistente.setActivo(true);
                // Actualizar datos con los del DTO
                usuarioExistente.setNombre(usuarioDTO.getNombre());
                usuarioExistente.setApellido(usuarioDTO.getApellido());
                usuarioExistente.setEdad(usuarioDTO.getEdad());
                usuarioExistente.setTelefono(usuarioDTO.getTelefono());
                usuarioExistente.setUsername(usuarioDTO.getUsername());
                usuarioExistente.setGeneros(usuarioDTO.getGeneros());

                // Actualizar contraseña en credenciales
                CredentialsEntity credentialsEntity = credencialOptional.get();
                credentialsEntity.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

                // Guardar cambios
                usuarioRepository.save(usuarioExistente);
                credentialsRepository.save(credentialsEntity);
                throw new UsuarioYaExisteException("Se volvió a dar de alta al usuario con mail: " + usuarioDTO.getEmail() + " con la contraseña y datos que ingreso recientemente");
            } else {
                throw new UsuarioYaExisteException("Ya existe un usuario activo con el email especificado.");
            }
        }

        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new UsuarioYaExisteException("Ya existe un usuario con el username especificado");
        }
    }
    // crear un nuevo usuario
    public void save(NewUsuarioDTO usuarioDTO) {

        validarUsuarioExistente(usuarioDTO);

        UsuarioEntity usuario = usuarioMapper.convertToNewEntity(usuarioDTO);

        CredentialsEntity credencial = new CredentialsEntity();
        credencial.setEmail(usuarioDTO.getEmail());
        credencial.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        credencial.setUsuario(usuario);

        RoleEntity roleUser = roleRepository.findByRole(Role.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No se encontró el rol USER"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);
        credencial.setRoles(roles);

        usuario.setCredencial(credencial);

        usuarioRepository.save(usuario);
    }

    public UsuarioDTO mostrarMiPerfil(UsuarioEntity usuarioEntity)
    {
        if(usuarioEntity.getActivo())
        {
            return usuarioMapper.convertToDTO(usuarioEntity);
        } else {
            throw new UsuarioNoEncontradoException("El usuario no se encuentra activo.");
        }
    }

    // buscar usuario por ID
    public UsuarioDTO findById(long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró el usuario con el id: " + id));
    }

    // buscar usuario por username
    public UsuarioDTO findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encuentra un usuario con el username: " + username));
    }

    // actualizar datos de usuario
    //tengo q hacer verificacion de que no me deje modificar a un usuario q no sea el q inicio sesion
    public void actualizarUsuario(Long id, UsuarioModificarDTO nuevosDatos) {
        UsuarioEntity existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró el usuario con el id: " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity usuarioAutenticado = getUsuarioAutenticado();

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin && !usuarioAutenticado.getId().equals(id)) {
            throw new AccessDeniedException("No tenés permiso para modificar datos este usuario.");
        }

        if (nuevosDatos.getTelefono() != null) {
            existente.setTelefono(nuevosDatos.getTelefono());
        }
        if (nuevosDatos.getUsername() != null) {
            existente.setUsername(nuevosDatos.getUsername());
        }
        if(nuevosDatos.getPassword() != null)
        {
            existente.getCredencial().setPassword(passwordEncoder.encode(nuevosDatos.getPassword()));
        }
        if(nuevosDatos.getGeneros() != null)
        {
            nuevosDatos.getGeneros()
                            .forEach(g -> existente.getGeneros().add(g));

        }

        usuarioRepository.save(existente);
    }

    // cambiar estado de usuario (activar/desactivar)
    public void cambiarEstadoUsuario(Long id, boolean activo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity usuarioAutenticado = getUsuarioAutenticado();

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin && !usuarioAutenticado.getId().equals(id)) {
            throw new AccessDeniedException("No tenés permiso para desactivar este usuario.");
        }

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    // listar todos los usuarios paginados
    public Page<UsuarioDTO> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }

    // listar usuarios mayores a una edad
    public Page<UsuarioDTO> usuariosMayores(Long edad, Pageable pageable) {
        return usuarioRepository.findByEdadGreaterThan(edad, pageable)
                .map(usuarioMapper::convertToDTO);
    }

    // obtener usuario DTO por id
    public UsuarioDTO getUsuarioDTO(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró el usuario con el id: " + id));
    }

    // obtener usuarios paginados
    public Page<UsuarioDTO> obtenerUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::convertToDTO);
    }

    // buscamos usuarios con filtros
    public Page<UsuarioDTO> buscarUsuarios(String nombre, String apellido, String email, String username, Boolean activo, Pageable pageable) {
        Specification<UsuarioEntity> spec = Specification
                .where(UsuarioSpecification.nombre(nombre))
                .and(UsuarioSpecification.apellido(apellido))
                .and(UsuarioSpecification.email(email))
                .and(UsuarioSpecification.username(username))
                .and(UsuarioSpecification.activo(activo));

        Page<UsuarioEntity> page = usuarioRepository.findAll(spec, pageable);

        if (page.getContent().isEmpty()) {
            throw new UsuarioNoEncontradoException("No se encontraron usuarios con los filtros especificados.");
        }

        return page.map(usuarioMapper::convertToDTO);
    }

    // obtener usuario autenticado desde el contexto de seguridad
    public UsuarioEntity getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CredentialsEntity credencialAutenticada = credentialsRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario autenticado no encontrado."));
        return credencialAutenticada.getUsuario();
    }

    public Page<ContenidoMostrarDTO> obtenerLikes(Long id, Pageable pageable) {
        return usuarioRepository.findLikes(id, pageable).map(contenidoMapper::convertToDTOForAdmin);
    }
}
