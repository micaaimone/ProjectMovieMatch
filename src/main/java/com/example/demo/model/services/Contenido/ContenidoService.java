package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoCompletoDTO;
import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoYaAgregadoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.mappers.Contenido.PeliculaMapper;
import com.example.demo.model.mappers.Contenido.SerieMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.ContenidoLikeRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.services.Usuarios.ContenidoLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final ContenidoLikeService contenidoLikeService;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoLikeRepository contenidoLikeRepository;
    private final ContenidoMapper contenidoMapper;
    private final APIMovieService apiMovieService;
    private final PeliculaMapper peliculaMapper;
    private final SerieMapper serieMapper;

    @Autowired
    public ContenidoService(
            ContenidoRepository contenidoRepository,
            ContenidoLikeService contenidoLikeService,
            UsuarioRepository usuarioRepository,
            ContenidoLikeRepository contenidoLikeRepository,
            ContenidoMapper contenidoMapper,
            APIMovieService apiMovieService,
            PeliculaMapper peliculaMapper,
            SerieMapper serieMapper
    ) {
        this.contenidoRepository = contenidoRepository;
        this.contenidoLikeService = contenidoLikeService;
        this.usuarioRepository = usuarioRepository;
        this.contenidoLikeRepository = contenidoLikeRepository;
        this.contenidoMapper = contenidoMapper;
        this.apiMovieService = apiMovieService;
        this.peliculaMapper = peliculaMapper;
        this.serieMapper = serieMapper;
    }

    // ========================== CRUD Y FILTRADO ==========================

    public Page<ContenidoDTO> buscarActivos(Pageable pageable, String genero, String anio, String titulo, Double puntuacion, Boolean activo, String clasificacion, Long id) {
        Specification<ContenidoEntity> specification = Specification
                .where(ContenidoSpecification.genero(genero))
                .and(ContenidoSpecification.anio(anio))
                .and(ContenidoSpecification.tituloParecido(titulo))
                .and(ContenidoSpecification.puntuacion(puntuacion))
                .and(ContenidoSpecification.activo(activo))
                .and(ContenidoSpecification.clasificacion(clasificacion))
                .and(ContenidoSpecification.id(id));

        Page<ContenidoEntity> page = contenidoRepository.findAll(specification, pageable);

        if (page.getContent().isEmpty()) {
            throw new ContenidoNotFound("No se encontraron contenidos con los filtros especificados.");
        }

        return page.map(contenidoMapper::convertToDTO);
    }

    private ContenidoEntity obtenerContenidoPorId(long id) {
        return contenidoRepository.findById(id)
                .orElseThrow(() -> new ContenidoNotFound("No se encontró contenido con id: " + id));
    }

    public void darDeBajaContenido(long id, int page, int size) {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if (contenido.isActivo()) {
            contenido.setActivo(false);
            contenidoLikeService.quitarLikesPorBajaLogica(id, page, size);
            contenidoRepository.save(contenido);
        } else {
            throw new ContenidoNotFound("No se encuentra un contenido con el id: " + id);
        }
    }

    public void darDeAltaContenido(long id) {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if (contenido.isActivo()) {
            throw new ContenidoYaAgregadoException("El contenido ya fue agregado a la base de datos");
        } else {
            contenido.setActivo(true);
            contenidoRepository.save(contenido);
        }
    }

    public ContenidoCompletoDTO buscarContenidoPorNombreDesdeAPI(String titulo) {
        ContenidoCompletoDTO contenido = apiMovieService.findContenidoByTitle(titulo);

        if (contenido == null || contenido.getTitulo() == null) {
            throw new ContenidoNotFound("No se encontró ningún contenido con el título: " + titulo);
        }

        String tipo = contenido.getTipo();

        switch (tipo.toLowerCase()) {
            case "movie":
                PeliculaEntity pelicula = peliculaMapper.convertFromContenidoCompletoToEntity(contenido);
                contenidoRepository.save(pelicula);
                break;
            case "series":
                SerieEntity serie = serieMapper.convertFromContenidoCompletoToSerieEntity(contenido);
                contenidoRepository.save(serie);
                break;
            default:
                throw new RuntimeException("Tipo de contenido desconocido: " + tipo);
        }

        return contenido;
    }

    // ========================== RECOMENDACIONES ==========================

    public Page<ContenidoDTO> obtenerRecomendaciones(Long usuarioId, int page, int size) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        List<String> generosLikeados = contenidoLikeRepository.obtenerGenerosLikeadosPorUsuario(usuarioId);
        Pageable pageable = PageRequest.of(page, size);

        Page<ContenidoEntity> contenidos;

        if (!generosLikeados.isEmpty()) {
            contenidos = contenidoRepository.recomendarContenidoPorGeneroYEdad(generosLikeados, pageable);
            if (!contenidos.hasContent()) {
                contenidos = obtenerFallbackPorGeneroFavorito(usuario, pageable);
            }
        } else {
            contenidos = obtenerFallbackPorGeneroFavorito(usuario, pageable);
        }

        return contenidos.map(contenidoMapper::convertToDTO);
    }

    public Page<ContenidoEntity> obtenerFallbackPorGeneroFavorito(UsuarioEntity usuario, Pageable pageable) {
        List<String> generos = usuario.getGeneros()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        if (generos.isEmpty()) {
            return Page.empty(pageable);
        }

        return contenidoRepository.recomendarContenidoPorGeneroYEdad(generos, pageable);
    }
}
