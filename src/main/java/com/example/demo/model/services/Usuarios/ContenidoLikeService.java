package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Resenia.ContenidoLikeDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.LikeExceptions.LikeAlreadyExistsException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.user.ReseniaLikeMapper;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.ContenidoLikeRepository;
import com.example.demo.model.repositories.Usuarios.ReseniaLikeRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ContenidoLikeService {

    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;
    private final ContenidoLikeRepository contenidoLikeRepository;
    private final ReseniaLikeMapper reseniaLikeMapper;

    public ContenidoLikeService(UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ContenidoLikeRepository contenidoLikeRepository, ReseniaLikeMapper reseniaLikeMapper) {
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.contenidoLikeRepository = contenidoLikeRepository;
        this.reseniaLikeMapper = reseniaLikeMapper;
    }

    public void darLike(Long usuarioId, Long contenidoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ContenidoEntity contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        if(!contenido.isActivo())
        {
            throw new ContenidoNotFound("Contenido no encontrado");
        }

        if (contenidoLikeRepository.existsByUsuarioAndContenido(usuario, contenido)) {
            throw new LikeAlreadyExistsException("Ya diste like a este contenido");
        }

        ContenidoLikeEntity like = new ContenidoLikeEntity();
        like.setUsuario(usuario);
        like.setContenido(contenido);
        like.setFechaLike(LocalDateTime.now());
        contenidoLikeRepository.save(like);
    }

    public boolean quitarLike(Long usuarioId, Long contenidoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ContenidoEntity contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        if(!contenido.isActivo())
        {
            throw new ContenidoNotFound("Contenido no encontrado");
        }

        Optional<ContenidoLikeEntity> likeOpt = contenidoLikeRepository.findByUsuarioAndContenido(usuario, contenido);
        if (likeOpt.isPresent()) {
            contenidoLikeRepository.delete(likeOpt.get());
            return true;
        }
        return false;
    }


    public Page<ContenidoLikeDTO> obtenerLikes(Long usuarioId, int page, int size) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);

        // traemos las reseñas que el usuario likéo
        Page<ContenidoLikeEntity> likesPage = contenidoLikeRepository.findAllByUsuario(usuario, pageable);

        // mapeamos a DTOs

        return likesPage.map(reseniaLikeMapper::convertToDTO);
    }

    public void quitarLikesPorBajaLogica(Long idContenido, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContenidoLikeEntity> likesPage;

        ContenidoEntity contenido = contenidoRepository.findById(idContenido)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        do {
            likesPage = contenidoLikeRepository.findAllByContenido(contenido, pageable);
            if (!likesPage.isEmpty()) {
                contenidoLikeRepository.deleteAll(likesPage.getContent());
            }
            pageable = likesPage.hasNext() ? likesPage.nextPageable() : null;
        } while (pageable != null);
    }



}
