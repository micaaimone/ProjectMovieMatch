package com.example.demo.model.services;

import com.example.demo.model.DTOs.ContenidoDTO;
import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.DTOs.SerieDTO;
import com.example.demo.model.entities.PeliculaEntity;
import com.example.demo.model.entities.SerieEntity;
import com.example.demo.model.mappers.ContenidoMapper;
import com.example.demo.model.mappers.PeliculaMapper;
import com.example.demo.model.mappers.SerieMapper;
import com.example.demo.model.repositories.ContenidoRepository;
import com.example.demo.model.repositories.PeliculaRepository;
import com.example.demo.model.repositories.RatingRepository;
import com.example.demo.model.repositories.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ContenidoService {

    private final APIMovieService apiMovieService;
    private final ContenidoRepository contenidoRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final RatingRepository ratingRepository;
    private final PeliculaMapper peliculaMapper;
    private final SerieMapper serieMapper;
    private final ContenidoMapper contenidoMapper;

    @Autowired
    public ContenidoService(APIMovieService apiMovieService, ContenidoRepository contenidoRepository, PeliculaRepository peliculaRepository, SerieRepository serieRepository, RatingRepository ratingRepository, PeliculaMapper peliculaMapper, SerieMapper serieMapper, ContenidoMapper contenidoMapper) {
        this.apiMovieService = apiMovieService;
        this.contenidoRepository = contenidoRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.ratingRepository = ratingRepository;
        this.peliculaMapper = peliculaMapper;
        this.serieMapper = serieMapper;
        this.contenidoMapper = contenidoMapper;
    }

    // Listas de títulos de prueba
    private final List<String> peliculas = List.of(
            "The Shawshank Redemption",
            "The Godfather",
            "The Dark Knight",
            "Pulp Fiction",
            "Fight Club",
            "Inception",
            "Interstellar",
            "Forrest Gump",
            "The Matrix",
            "Gladiator"
    );


    private final List<String> series = List.of(
            "Breaking Bad",
            "Game of Thrones",
            "Stranger Things",
            "The Office",
            "Sherlock",
            "The Sopranos",
            "Friends",
            "The Crown",
            "The Mandalorian",
            "House of Cards"
    );




    public boolean checkPeliBDD(String imdbId) {
        return peliculaRepository.findAll().stream()
                .anyMatch(p -> p.getImdbId().equals(imdbId));
    }

    public boolean checkSerieBDD(String imdbId) {
        return serieRepository.findAll().stream()
                .anyMatch(s -> s.getImdbId().equals(imdbId));
    }

    public void traerSeriesAPI(){
        List<SerieEntity> contenidoSerie = new ArrayList<>();
        List<String> titulos = new ArrayList<>();

        titulos.addAll(series);

        // uso streams para recorrer la lista
        titulos.stream()
                .map(apiMovieService::findSerieByTitle)
                .filter(Objects::nonNull) // filtro nulos por si alguna consulta falla
                .filter(serie -> !checkSerieBDD(serie.getImdbId()))
                .forEach(serie -> {
                    System.out.println(serie);
                    // verifico  la lista de ratings no está vacía,
                    // sino, guarda cada uno de los ratings
                    if (serie.getRatings() != null) {
                        serie.getRatings()
                                .forEach(ratingRepository::save);
                    }
                    serieRepository.save(serie);// Guardar en su repo correcto
                    contenidoSerie.add(serie);
                });

    }


    public void traerPeliculaAPI()
    {
        List<PeliculaEntity> contenidoPelicula = new ArrayList<>();

        List<String> titulos = new ArrayList<>();
        titulos.addAll(peliculas);

        // uso streams para recorrer la lista
        titulos.stream()
                .map(apiMovieService::findPeliculaByTitle)
                .filter(Objects::nonNull) // filtro nulos por si alguna consulta falla
                .filter(peli -> !checkPeliBDD(peli.getImdbId()))
                .forEach(pelicula -> {
                    System.out.println(pelicula);
                    // verifico  la lista de ratings no está vacía,
                    // sino, guarda cada uno de los ratings
                    if (pelicula.getRatings() != null) {
                        pelicula.getRatings()
                                .forEach(ratingRepository::save);
                    }
                    peliculaRepository.save(pelicula);

                    contenidoPelicula.add(pelicula);// guardamos en la bdd
                });

    }

    public Page<PeliculaDTO> datosPeliculaBDD(Pageable pageable)
    {
        traerPeliculaAPI();
        return peliculaRepository.findAll(pageable)
                .map(peliculaMapper::convertToDTO);
    }

    public Page<SerieDTO> datosSerieBDD(Pageable pageable)
    {
        traerSeriesAPI();
        return  serieRepository.findAll(pageable)
                .map(serieMapper::convertToDTO);
    }

    public Page<ContenidoDTO> datosBDD(Pageable pageable)
    {
        traerPeliculaAPI();
        traerSeriesAPI();
        return contenidoRepository.findAll(pageable)
                .map(contenidoMapper::convertToDTO);
    }
}
