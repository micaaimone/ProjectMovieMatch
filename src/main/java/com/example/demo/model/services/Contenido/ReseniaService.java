package com.example.demo.model.services.Contenido;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaAlredyExists;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaNotFound;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.Contenido.ReseniaMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Contenido.ReseniaRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;
    private final ReseniaMapper reseniaMapper;
    private final CredentialsRepository credentialsRepository;

    @Autowired
    public ReseniaService(ReseniaRepository reseniaRepository, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ReseniaMapper reseniaMapper, CredentialsRepository credentialsRepository) {
        this.reseniaRepository = reseniaRepository;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.reseniaMapper = reseniaMapper;
        this.credentialsRepository = credentialsRepository;
    }



    public void save(ReseniaDTO dto)
    {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameAutenticado = authentication.getName();

        CredentialsEntity credencialAutenticada = credentialsRepository.findByEmail(usernameAutenticado)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));

        UsuarioEntity usuarioAutenticado = credencialAutenticada.getUsuario();

        // Validar que la reseña la está intentando hacer el mismo usuario autenticado
        if (!usuarioAutenticado.getId().equals(dto.getId_usuario())) {
            throw new AccessDeniedException("No tenés permiso para realizar una reseña desde este usuario.");
        }

        UsuarioEntity usuario = existeUsuario(dto);

        ContenidoEntity contenido = existeContenido(dto);

        if(!contenido.isActivo())
        {
            throw new ContenidoNotFound("No se encontro el contenido con id " + contenido.getId_contenido());
        }

        if (reseniaRepository.findByIDAndContenido(dto.getId_usuario(), dto.getId_contenido()).isEmpty())
        {
            ReseniaEntity resenia = reseniaMapper.convertToEntity(dto, usuario, contenido);

            reseniaRepository.save(resenia);
        } else
        {
            throw new ReseniaAlredyExists("ya hay una reseña creada por este usuario en este contenido");
        }

    }

    public UsuarioEntity existeUsuario(ReseniaDTO dto)
    {

        return usuarioRepository.findById(dto.getId_usuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    }

    public ContenidoEntity existeContenido(ReseniaDTO dto)
    {
        return contenidoRepository.findById(dto.getId_contenido())
            .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));
    }


    public void delete(Long id) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameAutenticado = authentication.getName();

        CredentialsEntity credencialAutenticada = credentialsRepository.findByEmail(usernameAutenticado)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));

        UsuarioEntity usuarioAutenticado = credencialAutenticada.getUsuario();

        // Validar que la reseña la está intentando hacer el mismo usuario autenticado
        if (!usuarioAutenticado.getId().equals(id)) {
            throw new AccessDeniedException("No tenés permiso para realizar una reseña desde este usuario.");
        }

        boolean resenia = reseniaRepository.existsById(id);

        if (resenia)
        {
            reseniaRepository.deleteById(id);
        } else
        {
            throw new ReseniaNotFound("No existe una reseña con ese id");
        }
    }

    @Transactional
    public void delete(Long id_u, Long id_c)
    {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameAutenticado = authentication.getName();

        CredentialsEntity credencialAutenticada = credentialsRepository.findByEmail(usernameAutenticado)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));

        UsuarioEntity usuarioAutenticado = credencialAutenticada.getUsuario();

        // Validar que la reseña la está intentando hacer el mismo usuario autenticado
        if (!usuarioAutenticado.getId().equals(id_u)) {
            throw new AccessDeniedException("No tenés permiso para realizar una reseña desde este usuario.");
        }


        ReseniaEntity resenia = reseniaRepository.findByIDAndContenido(id_u, id_c)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrada"));

        //los tengo q borrar manualmente xq reseñas es "el hijo"
        UsuarioEntity usuario = resenia.getUsuario();
        usuario.getReseñasHechas().remove(resenia);

        ContenidoEntity contenido = resenia.getContenido();
        contenido.getReseña().remove(resenia);

        //x si acaso guardo los cambios
        usuarioRepository.save(usuario);
        contenidoRepository.save(contenido);

        //ahora si borro la reseña
        reseniaRepository.delete(resenia);
    }

    public Page<ReseniaDTO> listarReseniasPorUsuario(Long id_usuario, Pageable pageable)
    {
        Page<ReseniaEntity> page = reseniaRepository.findAllById(id_usuario, pageable);


        if (page.getContent().isEmpty()) {
            throw new ReseniaNotFound("No se encontraron reseñas para este usuario.");
        }

        return page.map(reseniaMapper::convertToDTO);
    }

    public void modificarResenia( Long id_usuario, Long id_contenido, ReseniaModificarDTO dto) {
        if (dto.getComentario() == null && dto.getPuntuacionU() == null) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }

        ReseniaEntity resenia = reseniaRepository.findByIDAndContenido(id_usuario, id_contenido)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrada"));

        if (dto.getPuntuacionU() != null) {
            resenia.setPuntuacionU(dto.getPuntuacionU());
        }
        if (dto.getComentario() != null) {
            resenia.setComentario(dto.getComentario());
        }

        reseniaRepository.save(resenia);
    }

    @Transactional
    public void quitarReseniaPorBajaLogica(Long idContenido, int page, int size) {
        ContenidoEntity contenido = contenidoRepository.findById(idContenido)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        Page<ReseniaEntity> reseniasPage;

        do {
            reseniasPage = reseniaRepository.findAllByContenido(contenido, pageable);
            if (!reseniasPage.isEmpty()) {
                reseniaRepository.deleteAll(reseniasPage.getContent());
            }
            pageable = reseniasPage.hasNext() ? reseniasPage.nextPageable() : null;
        } while (pageable != null);
    }
}
