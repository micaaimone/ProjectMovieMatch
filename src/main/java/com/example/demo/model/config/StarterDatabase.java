package com.example.demo.model.config;

import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.repositories.Contenido.RatingRepository;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.repositories.subs.PlanRepository;
import com.example.demo.model.repositories.subs.SuscripcionRepository;
import com.example.demo.model.services.Contenido.APIMovieService;
import com.example.demo.model.services.subs.PlanService;
import com.example.demo.model.services.subs.SuscripcionService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class StarterDatabase {

    private final APIMovieService apiMovieService;
    private final RatingRepository ratingRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;

    public StarterDatabase(APIMovieService apiMovieService, RatingRepository ratingRepository, PeliculaRepository peliculaRepository,
                           SerieRepository serieRepository, PlanRepository planRepository, SuscripcionRepository suscripcionRepository) {
        this.apiMovieService = apiMovieService;
        this.ratingRepository = ratingRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
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

    @PostConstruct
    private void init()
    {
        traerPeliculaAPI();
        traerSeriesAPI();
        initPlan();
        validarSubs();

    }


    public void initPlan(){
        if (planRepository.count() == 0) {
            planRepository.save(new PlanSuscripcionEntity(1L, TipoSuscripcion.MENSUAL, 3000, null));
            planRepository.save(new PlanSuscripcionEntity(1l, TipoSuscripcion.SEMESTRAL, 15000, null));
            planRepository.save(new PlanSuscripcionEntity(1l, TipoSuscripcion.ANUAL, 25000, null));
        }
    }
    public void validarSubs(){
        suscripcionRepository.VerificarSuscripcion(LocalDate.now());
    }


    public void traerSeriesAPI(){
        List<SerieEntity> contenidoSerie = new ArrayList<>();
        List<String> titulos = new ArrayList<>();

        titulos.addAll(series);

        for (SerieEntity serie : titulos.stream()
                .map(apiMovieService::findSerieByTitle)
                .filter(Objects::nonNull)
                .filter(s -> !checkSerieBDD(s.getImdbId()))
                .toList()) {

            // guardo la serie primero para obtener su id_contenido
            serieRepository.save(serie);

            // guardo sus ratings y los asocio a la serie guardada
            if (serie.getRatings() != null) {
                for (RatingEntity rating : serie.getRatings()) {
                    rating.setContenido(serie);
                    ratingRepository.save(rating);
                }
            }

            contenidoSerie.add(serie);
        }
    }


    public void traerPeliculaAPI()
    {
        List<PeliculaEntity> contenidoPelicula = new ArrayList<>();

        List<String> titulos = new ArrayList<>();
        titulos.addAll(peliculas);

        // uso streams para recorrer la lista
        for (PeliculaEntity pelicula : titulos.stream()
                .map(apiMovieService::findPeliculaByTitle)
                .filter(Objects::nonNull)
                .filter(p -> !checkPeliBDD(p.getImdbId()))
                .toList()) {

            //seteo que automaticamente el estado sea 0 (activo)
            pelicula.setEstado(0);
            // guardo la serie primero para obtener su id_contenido
            peliculaRepository.save(pelicula);

            // guardo sus ratings y los asocio a la serie guardada
            if (pelicula.getRatings() != null) {
                for (RatingEntity rating : pelicula.getRatings()) {
                    rating.setContenido(pelicula);
                    ratingRepository.save(rating);
                }
            }

            contenidoPelicula.add(pelicula);
        }
    }

    public void ValidarSub(){

    }

    public boolean checkPeliBDD(String imdbId) {
        return peliculaRepository.findAll().stream()
                .anyMatch(p -> p.getImdbId().equals(imdbId));
    }

    public boolean checkSerieBDD(String imdbId) {
        return serieRepository.findAll().stream()
                .anyMatch(s -> s.getImdbId().equals(imdbId));
    }

}
