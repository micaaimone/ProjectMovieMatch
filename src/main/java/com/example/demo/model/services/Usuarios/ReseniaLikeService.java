package com.example.demo.model.services.Usuarios;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.User.ContenidoLike;
import com.example.demo.model.entities.User.ReseniaLike;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaNotFound;
import com.example.demo.model.exceptions.UsuarioExceptions.LikeAlreadyExistsException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.repositories.Contenido.ReseniaRepository;
import com.example.demo.model.repositories.Usuarios.ReseniaLikeRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReseniaLikeService {

    private final UsuarioRepository usuarioRepository;
    private final ReseniaRepository reseniaRepository;
    private final ReseniaLikeRepository reseniaLikeRepository;

    public ReseniaLikeService(UsuarioRepository usuarioRepository, ReseniaRepository reseniaRepository, ReseniaLikeRepository reseniaLikeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reseniaRepository = reseniaRepository;
        this.reseniaLikeRepository = reseniaLikeRepository;
    }

    public void darLike(Long usuarioId, Long reseniaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ReseniaEntity resenia = reseniaRepository.findById(reseniaId)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrado"));

        if (reseniaLikeRepository.existsByUsuarioAndResenia(usuario, resenia)) {
            throw new LikeAlreadyExistsException("Ya diste like a esta reseña");
        }

        ReseniaLike like = new ReseniaLike();
        like.setUsuario(usuario);
        like.setResenia(resenia);
        like.setFechaLike(LocalDateTime.now());
        reseniaLikeRepository.save(like);
    }

    public boolean quitarLike(Long usuarioId, Long reseniaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ReseniaEntity resenia = reseniaRepository.findById(reseniaId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        Optional<ReseniaLike> likeOpt = reseniaLikeRepository.findByUsuarioAndResenia(usuario, resenia);
        if (likeOpt.isPresent()) {
            reseniaLikeRepository.delete(likeOpt.get());
            return true;
        }
        return false;
    }

    public Page<ReseniaLike> obtenerLikes(Long usuarioId, int page, int size) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return reseniaLikeRepository.findAllByUsuario(usuario, pageable);
    }
}
