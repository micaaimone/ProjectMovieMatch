package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Resenia.ReseniaLikeDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.example.demo.model.entities.User.ReseniaLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.LikeExceptions.LikeAlreadyExistsException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.user.ReseniaLikeMapper;
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
    private final ReseniaLikeRepository reseniaLikeRepository;
    private final ReseniaLikeMapper reseniaLikeMapper;

    public ContenidoLikeService(UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ContenidoLikeRepository contenidoLikeRepository, ReseniaLikeRepository reseniaLikeRepository, ReseniaLikeMapper reseniaLikeMapper) {
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.contenidoLikeRepository = contenidoLikeRepository;
        this.reseniaLikeRepository = reseniaLikeRepository;
        this.reseniaLikeMapper = reseniaLikeMapper;
    }

    public void darLike(Long usuarioId, Long contenidoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ContenidoEntity contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

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

        Optional<ContenidoLikeEntity> likeOpt = contenidoLikeRepository.findByUsuarioAndContenido(usuario, contenido);
        if (likeOpt.isPresent()) {
            contenidoLikeRepository.delete(likeOpt.get());
            return true;
        }
        return false;
    }

    public Page<ReseniaLikeDTO> obtenerLikes(Long usuarioId, int page, int size) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);

        // traemos las reseñas que el usuario likéo
        Page<ReseniaLikeEntity> likesPage = reseniaLikeRepository.findAllByUsuario(usuario, pageable);

        // mapeamos a DTOs
        Page<ReseniaLikeDTO> dtoPage = likesPage.map(reseniaLikeMapper::convertToDTO);

        return dtoPage;
    }


}
