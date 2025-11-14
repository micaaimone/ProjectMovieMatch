package com.example.demo.model.services.Usuarios;

import com.example.demo.model.entities.User.ListasContenidoEntity;
import com.example.demo.model.enums.TipoReaccion;
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
import com.example.demo.model.repositories.Usuarios.ListasContenidoRepository;
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
    private final ListasContenidoRepository listasContenidoRepository;

    public ContenidoLikeService(UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ContenidoLikeRepository contenidoLikeRepository, ReseniaLikeMapper reseniaLikeMapper, ListasContenidoRepository listasContenidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.contenidoLikeRepository = contenidoLikeRepository;
        this.reseniaLikeMapper = reseniaLikeMapper;
        this.listasContenidoRepository = listasContenidoRepository;
    }

    public void darLike(Long usuarioId, Long contenidoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ContenidoEntity contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        if (!contenido.isActivo()) {
            throw new ContenidoNotFound("Contenido no encontrado");
        }

        // Buscamos si ya existe una reacción (like o dislike)
        Optional<ContenidoLikeEntity> reaccionOpt =
                contenidoLikeRepository.findByUsuarioAndContenido(usuario, contenido);

        if (reaccionOpt.isPresent()) {
            ContenidoLikeEntity reaccion = reaccionOpt.get();

            if (reaccion.getTipo() == TipoReaccion.LIKE) {
                throw new LikeAlreadyExistsException("Ya diste like a este contenido");
            }

            // Si antes era dislike, lo actualizamos a like
            reaccion.setTipo(TipoReaccion.LIKE);
            reaccion.setFechaLike(LocalDateTime.now());
            contenidoLikeRepository.save(reaccion);
        } else {
            // Si no había reacción previa, creamos una nueva
            ContenidoLikeEntity like = new ContenidoLikeEntity();
            like.setUsuario(usuario);
            like.setContenido(contenido);
            like.setTipo(TipoReaccion.LIKE);
            like.setFechaLike(LocalDateTime.now());
            contenidoLikeRepository.save(like);
        }
        // ====== AGREGAR A LISTA "FAVORITOS" AUTOMÁTICAMENTE ======
        agregarAFavoritos(usuario, contenido);
    }

    private void agregarAFavoritos(UsuarioEntity usuario, ContenidoEntity contenido) {
        // Buscar la lista "Favoritos" del usuario
        Optional<ListasContenidoEntity> favoritosOpt =
                listasContenidoRepository.findByNombre(usuario.getId(), "Favoritos");

        if (favoritosOpt.isPresent()) {
            ListasContenidoEntity favoritos = favoritosOpt.get();

            // Solo agregar si no está ya en la lista
            if (!favoritos.getContenidos().contains(contenido)) {
                favoritos.getContenidos().add(contenido);
                listasContenidoRepository.save(favoritos);
            }
        } else {
            // ====== CREAR LISTA "FAVORITOS" AUTOMÁTICAMENTE ======
            ListasContenidoEntity listaFavoritos = new ListasContenidoEntity();
            listaFavoritos.setNombre("Favoritos");
            listaFavoritos.setUsuario(usuario);
            listaFavoritos.setPrivado(false);
            listasContenidoRepository.save(listaFavoritos);
        }

    }


    public void dislike(Long usuarioId, Long contenidoId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        ContenidoEntity contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        if (!contenido.isActivo()) {
            throw new ContenidoNotFound("Contenido no encontrado");
        }

        // Verificar si ya existe una reacción
        Optional<ContenidoLikeEntity> reaccionOpt =
                contenidoLikeRepository.findByUsuarioAndContenido(usuario, contenido);

        if (reaccionOpt.isPresent()) {
            ContenidoLikeEntity reaccion = reaccionOpt.get();
            reaccion.setTipo(TipoReaccion.DISLIKE);
            reaccion.setFechaLike(LocalDateTime.now());
            contenidoLikeRepository.save(reaccion);
        } else {
            ContenidoLikeEntity dislike = new ContenidoLikeEntity();
            dislike.setUsuario(usuario);
            dislike.setContenido(contenido);
            dislike.setTipo(TipoReaccion.DISLIKE);
            dislike.setFechaLike(LocalDateTime.now());
            contenidoLikeRepository.save(dislike);
        }
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
