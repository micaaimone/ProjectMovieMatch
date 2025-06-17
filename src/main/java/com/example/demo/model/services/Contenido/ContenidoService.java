package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.enums.Genero;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoYaAgregadoException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.ContenidoLikeRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoLikeRepository contenidoLikeRepository;
    private final ContenidoMapper contenidoMapper;
    private final APIMovieService apiMovieService;


    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository, UsuarioRepository usuarioRepository, ContenidoLikeRepository contenidoLikeRepository, ContenidoMapper contenidoMapper, APIMovieService apiMovieService) {
        this.contenidoRepository = contenidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.contenidoLikeRepository = contenidoLikeRepository;
        this.contenidoMapper = contenidoMapper;
        this.apiMovieService = apiMovieService;
    }


    public Page<ContenidoDTO> buscarActivos(Pageable pageable,String genero, String anio, String titulo,Double puntuacion, Boolean activo, String clasificacion, Long id){
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



    public void darDeBajaContenido(long id) {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if(contenido.isActivo())
        {
            contenido.setActivo(false);
            contenidoRepository.save(contenido);
        } else {
            throw new ContenidoNotFound("No se encuentra un contenido con el id: " + id);
        }

    }



    public void darDeAltaContenido(long id)
    {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if(contenido.isActivo()) {
            throw new ContenidoYaAgregadoException("El contenido ya fue agregado a la base de datos");
        } else {
            contenido.setActivo(true);
            contenidoRepository.save(contenido);
        }
    }


    public ContenidoDTO buscarContenidoPorNombreDesdeAPI(String titulo) {
        ContenidoEntity contenido = apiMovieService.findContenidoByTitle(titulo);

        if (contenido == null || contenido.getTitulo() == null) {
            throw new ContenidoNotFound("No se encontró ningún contenido con el título: " + titulo);
        }

        String tipo = contenido.getTipo();

        switch (tipo.toLowerCase()) {
            case "movie":
                System.out.println("Es una película");
                break;
            case "series":
                System.out.println("Es una serie");
                break;
            default:
                System.out.println("Otro tipo de contenido: " + tipo);
                break;
        }

        // Guardar en base de datos
        contenidoRepository.save(contenido);
        return contenidoMapper.convertToDTO(contenido);
    }

    // primeras reco (basarlo en los generos fav)


    // una vex likeadas
    public Page<ContenidoDTO> obtenerRecomendaciones(Long usuarioId, int page, int size) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        List<String> generosLikeados = contenidoLikeRepository.obtenerGenerosLikeadosPorUsuario(usuarioId);
        Pageable pageable = PageRequest.of(page, size);

        Page<ContenidoEntity> contenidos;

        if (!generosLikeados.isEmpty()) {
            contenidos = contenidoRepository.recomendarContenidoPorGeneroYEdad(
                    generosLikeados,
                    usuario.getEdad(),
                    pageable
            );

            // si devuelve vacío, muestra segun generos fav
            if (!contenidos.hasContent()) {
                contenidos = obtenerFallbackPorGeneroFavorito(usuario, pageable);
            }
        } else {
            contenidos = obtenerFallbackPorGeneroFavorito(usuario, pageable);
        }

        return contenidos.map(contenidoMapper::convertToDTO);
    }

    // metodo auxiliar
    public Page<ContenidoEntity> obtenerFallbackPorGeneroFavorito(UsuarioEntity usuario, Pageable pageable) {
        List<String> generos = usuario.getGeneros()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        if (generos.isEmpty()) {
            return Page.empty(pageable);
        }

        return contenidoRepository.recomendarContenidoPorGeneroYEdad(
                generos,
                usuario.getEdad(),
                pageable
        );
    }



}
