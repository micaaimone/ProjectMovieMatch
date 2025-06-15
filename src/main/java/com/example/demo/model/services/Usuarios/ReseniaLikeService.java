package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Resenia.ReseniaLikeDTO;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.ReseniaLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaNotFound;
import com.example.demo.model.exceptions.LikeExceptions.LikeAlreadyExistsException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.user.ReseniaLikeMapper;
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
    private final ReseniaLikeMapper reseniaLikeMapper;

    public ReseniaLikeService(UsuarioRepository usuarioRepository, ReseniaRepository reseniaRepository, ReseniaLikeRepository reseniaLikeRepository, ReseniaLikeMapper reseniaLikeMapper) {
        this.usuarioRepository = usuarioRepository;
        this.reseniaRepository = reseniaRepository;
        this.reseniaLikeRepository = reseniaLikeRepository;
        this.reseniaLikeMapper = reseniaLikeMapper;
    }

    public void darLike(Long usuarioId, Long reseniaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ReseniaEntity resenia = reseniaRepository.findById(reseniaId)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrado"));

        if (reseniaLikeRepository.existsByUsuarioAndResenia(usuario, resenia)) {
            throw new LikeAlreadyExistsException("Ya diste like a esta reseña");
        }

        ReseniaLikeEntity like = new ReseniaLikeEntity();
        like.setUsuario(usuario);
        like.setResenia(resenia);
        like.setFechaLike(LocalDateTime.now());
        reseniaLikeRepository.save(like);
        usuario.getReseniaLikes().add(like);
    }

    public boolean quitarLike(Long usuarioId, Long reseniaId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ReseniaEntity resenia = reseniaRepository.findById(reseniaId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        Optional<ReseniaLikeEntity> likeOpt = reseniaLikeRepository.findByUsuarioAndResenia(usuario, resenia);
        if (likeOpt.isPresent()) {
            ReseniaLikeEntity like = likeOpt.get();

            // eliminamos de la colección del usuario
            usuario.getReseniaLikes().remove(like);

            reseniaLikeRepository.delete(like);

            return true;
        }
        return false;
    }

    public Page<ReseniaLikeDTO> obtenerLikes(Long usuarioId,  int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        Page<ReseniaLikeEntity> likesPage = reseniaLikeRepository.findAllByUsuarioId(usuarioId, pageable);

        return likesPage.map(reseniaLikeMapper::convertToDTO);
    }



}
