package com.example.demo.model.config;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.entities.User.CredencialEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.enums.E_Cargo;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.repositories.Contenido.RatingRepository;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.repositories.Contenido.ReseniaRepository;
import com.example.demo.model.repositories.Subs.PlanRepository;
import com.example.demo.model.repositories.Subs.SuscripcionRepository;
import com.example.demo.model.repositories.Usuarios.CredencialRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.services.Contenido.APIMovieService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Configuration
public class StarterDatabase {

    private final APIMovieService apiMovieService;
    private final RatingRepository ratingRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final CredencialRepository credencialRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReseniaRepository reseñaRepository;
    private final ContenidoRepository contenidoRepository;


    public StarterDatabase(APIMovieService apiMovieService, RatingRepository ratingRepository, PeliculaRepository peliculaRepository,
                           SerieRepository serieRepository, PlanRepository planRepository, SuscripcionRepository suscripcionRepository, CredencialRepository credencialRepository, UsuarioRepository usuarioRepository, ReseniaRepository reseñaRepository, ContenidoRepository contenidoRepository) {
        this.apiMovieService = apiMovieService;
        this.ratingRepository = ratingRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.credencialRepository = credencialRepository;
        this.usuarioRepository = usuarioRepository;
        this.reseñaRepository = reseñaRepository;
        this.contenidoRepository = contenidoRepository;
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
        initCredenciales();
        initUsers();
        initReseñas();

    }
    public void initUsers() {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.saveAll(List.of(
                    UsuarioEntity.builder()
                            .nombre("Lautaro")
                            .apellido("Martínez")
                            .email("lautaro@example.com")
                            .edad(28)
                            .telefono(1123456789L)
                            .contrasenia("1234")
                            .username("lautaM")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Sofía")
                            .apellido("González")
                            .email("sofia@example.com")
                            .edad(24)
                            .telefono(1167891234L)
                            .contrasenia("abcd")
                            .username("sofiG")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Martín")
                            .apellido("Pérez")
                            .email("martin@example.com")
                            .edad(35)
                            .telefono(1134567890L)
                            .contrasenia("pass123")
                            .username("martinP")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Camila")
                            .apellido("López")
                            .email("camila@example.com")
                            .edad(22)
                            .telefono(1198765432L)
                            .contrasenia("qwerty")
                            .username("camiL")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Nicolás")
                            .apellido("Díaz")
                            .email("nico@example.com")
                            .edad(30)
                            .telefono(1187654321L)
                            .contrasenia("nico123")
                            .username("nikoD")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Valentina")
                            .apellido("Torres")
                            .email("valen@example.com")
                            .edad(27)
                            .telefono(1176543210L)
                            .contrasenia("valenpass")
                            .username("valenT")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Julián")
                            .apellido("Ramírez")
                            .email("julian@example.com")
                            .edad(33)
                            .telefono(1156781234L)
                            .contrasenia("julianpass")
                            .username("juliR")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Florencia")
                            .apellido("Castro")
                            .email("flor@example.com")
                            .edad(26)
                            .telefono(1199998888L)
                            .contrasenia("florcita")
                            .username("florC")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Diego")
                            .apellido("Morales")
                            .email("diego@example.com")
                            .edad(40)
                            .telefono(1177776666L)
                            .contrasenia("diego40")
                            .username("diegoM")
                            .activo(true)
                            .build(),

                    UsuarioEntity.builder()
                            .nombre("Agustina")
                            .apellido("Fernández")
                            .email("agus@example.com")
                            .edad(29)
                            .telefono(1144443333L)
                            .contrasenia("agus29")
                            .username("agusF")
                            .activo(true)
                            .build()
            ));
        }
    }

    private void initReseñas() {
        if (reseñaRepository.count() == 0) {

            List<UsuarioEntity> usuarios = usuarioRepository.findAll();
            List<ContenidoEntity> contenidos = contenidoRepository.findAll();

            reseñaRepository.saveAll(List.of(
                    ReseniaEntity.builder()
                            .usuario(usuarios.get(0))
                            .contenido(contenidos.get(0))
                            .puntuacionU(9.8)
                            .comentario("Increíble película, la volvería a ver.")
                            .fecha(LocalDateTime.now())
                            .build(),

                    ReseniaEntity.builder()
                            .usuario(usuarios.get(1))
                            .contenido(contenidos.get(1))
                            .puntuacionU(7.9)
                            .comentario("Obra maestra del cine.")
                            .fecha(LocalDateTime.now())
                            .build(),

                    ReseniaEntity.builder()
                            .usuario(usuarios.get(2))
                            .contenido(contenidos.get(2))
                            .puntuacionU(8.7)
                            .comentario("Buena, pero un poco larga.")
                            .fecha(LocalDateTime.now())
                            .build(),

                    ReseniaEntity.builder()
                            .usuario(usuarios.get(1))
                            .contenido(contenidos.get(2))
                            .puntuacionU(6)
                            .comentario("Meh, muy larga.")
                            .fecha(LocalDateTime.now())
                            .build(),

                    ReseniaEntity.builder()
                            .usuario(usuarios.get(3))
                            .contenido(contenidos.get(3))
                            .puntuacionU(8.9)                            .comentario("Excelente historia y actuaciones.")
                            .fecha(LocalDateTime.now())
                            .build()
            ));
        }
    }




    public void initPlan(){
        if (planRepository.count() == 0) {
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.MENSUAL, 3000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.SEMESTRAL, 15000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.ANUAL, 25000, null));
        }
    }

    public void initCredenciales() {
        if (!credencialRepository.existsById(1L)) {
            credencialRepository.save(new CredencialEntity(1L, E_Cargo.ADMIN));
        }
        if (!credencialRepository.existsById(2L)) {
            credencialRepository.save(new CredencialEntity(2L, E_Cargo.USUARIO_PREMIUM));
        }
        if (!credencialRepository.existsById(3L)) {
            credencialRepository.save(new CredencialEntity(3L, E_Cargo.USUARIO));
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

            //seteo que automaticamente el estado sea true
            serie.setActivo(true);
            // guardo la serie primero para obtener su id_contenido
            serieRepository.save(serie);

            // guardo sus ratings y los asocio a la serie guardada
            if (serie.getRatings() != null) {
                for (RatingEntity rating : serie.getRatings()) {
                    rating.setContenido(serie);
                    ratingRepository.save(rating);
                    serie.setPuntuacion(calcularPromedioSerie(serie));
                    serieRepository.save(serie);
                }
            }



            contenidoSerie.add(serie);
        }
    }

    public double calcularPromedioSerie(SerieEntity serie){
        double promedio = serie.getRatings()
                .stream()
                .mapToDouble(r -> {
                    String valor = r.getValor();
                    if (valor == null || valor.isEmpty() || valor.equals("N/A")) {
                        return 0.0;
                    }
                    if (valor.contains("/")) {
                        String[] parts = valor.split("/");
                        return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]) * 10;
                    } else if (valor.contains("%")) {
                        return Double.parseDouble(valor.replace("%", "")) / 10;
                    } else {
                        return Double.parseDouble(valor);
                    }
                })
                .average()
                .orElseThrow(() -> new NoSuchElementException("No hay ratings disponibles"));
        // Redondear a 2 decimales
        return Math.round(promedio * 100.0) / 100.0;
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

            //seteo que automaticamente el estado sea true
            pelicula.setActivo(true);
            // guardo la serie primero para obtener su id_contenido
            peliculaRepository.save(pelicula);

            // guardo sus ratings y los asocio a la serie guardada
            if (pelicula.getRatings() != null) {
                for (RatingEntity rating : pelicula.getRatings()) {
                    rating.setContenido(pelicula);
                    ratingRepository.save(rating);
                    pelicula.setPuntuacion(calcularPromedioPelicula(pelicula));
                    peliculaRepository.save(pelicula);
                }
            }

            contenidoPelicula.add(pelicula);
        }
    }

    public double calcularPromedioPelicula(PeliculaEntity pelicula){
        double promedio = pelicula.getRatings()
                .stream()
                .mapToDouble(r -> {
                    String valor = r.getValor();
                    if (valor == null || valor.isEmpty() || valor.equals("N/A")) {
                        return 0.0;
                    }
                    if (valor.contains("/")) {
                        String[] parts = valor.split("/");
                        return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]) * 10;
                    } else if (valor.contains("%")) {
                        return Double.parseDouble(valor.replace("%", "")) / 10;
                    } else {
                        return Double.parseDouble(valor);
                    }
                })
                .average()
                .orElseThrow(() -> new NoSuchElementException("No hay ratings disponibles"));
        // Redondear a 2 decimales
        return Math.round(promedio * 100.0) / 100.0;    }

    public boolean checkPeliBDD(String imdbId) {
        return peliculaRepository.findAll().stream()
                .anyMatch(p -> p.getImdbId().equals(imdbId));
    }

    public boolean checkSerieBDD(String imdbId) {
        return serieRepository.findAll().stream()
                .anyMatch(s -> s.getImdbId().equals(imdbId));
    }



}
