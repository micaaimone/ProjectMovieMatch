package com.example.demo.model.config;

import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.RoleRepository;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;

import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.repositories.Contenido.RatingRepository;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.repositories.Contenido.ReseniaRepository;
import com.example.demo.model.repositories.Subs.PlanRepository;
import com.example.demo.model.repositories.Subs.SuscripcionRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.services.Contenido.APIMovieService;
import com.example.demo.model.services.Email.EmailService;
import com.example.demo.model.services.Subs.SuscripcionService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.*;

@Configuration
public class StarterDatabase {

    private final APIMovieService apiMovieService;
    private final RatingRepository ratingRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final PlanRepository planRepository;
    private final SuscripcionService suscripcionService;
    private final UsuarioRepository usuarioRepository;
    private final ReseniaRepository reseñaRepository;
    private final ContenidoRepository contenidoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    public StarterDatabase(APIMovieService apiMovieService, RatingRepository ratingRepository,
                           PeliculaRepository peliculaRepository, SerieRepository serieRepository,
                           PlanRepository planRepository, SuscripcionService suscripcionService,
                           UsuarioRepository usuarioRepository, ReseniaRepository reseñaRepository,
                           ContenidoRepository contenidoRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository, EmailService emailService, UsuarioService usuarioService) {
        this.apiMovieService = apiMovieService;
        this.ratingRepository = ratingRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.planRepository = planRepository;
        this.suscripcionService = suscripcionService;
        this.usuarioRepository = usuarioRepository;
        this.reseñaRepository = reseñaRepository;
        this.contenidoRepository = contenidoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
        this.emailService = emailService;
        this.usuarioService = usuarioService;
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
    private void init() {

        traerPeliculaAPI();
        traerSeriesAPI();
        initPlan();
        initCredenciales();
        initUsers();
        initReseñas();

    }


    public void initUsers() {
        crearUsuarioSiNoExiste("lautaA", "Lautaro", "Martínez", 28, "1123456789", "lau9araya@gmail.com", Role.ROLE_USER);
        crearUsuarioSiNoExiste("meliR", "Melina", "Rodríguez", 25, "1123456790", "meli@gmail.com", Role.ROLE_PREMIUM);
        crearUsuarioSiNoExiste("tomiG", "Tomás", "Gómez", 30, "1123456791", "tomi@gmail.com", Role.ROLE_ADMIN);
        crearUsuarioSiNoExiste("sofiP", "Sofía", "Pérez", 27, "1123456792", "sofi@gmail.com", Role.ROLE_USER);
    }

    private void crearUsuarioSiNoExiste(String username, String nombre, String apellido, int edad, String telefono, String email, Role role) {
        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByUsername(username);
        UsuarioEntity usuario;

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        } else {
            usuario = UsuarioEntity.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .edad(edad)
                    .telefono(telefono)
                    .username(username)
                    .email(email)
                    .activo(true)
                    .build();

            usuario = usuarioRepository.save(usuario);
        }

        RoleEntity roleEntity = roleRepository.findByRole(role)
                .orElseThrow(() -> new EntityNotFoundException("Rol inexistente " + role));
        HashSet<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntity);

        if (usuario.getCredencial() == null) {
            CredentialsEntity credentialsEntity = CredentialsEntity.builder()
                    .email(email)
                    .password(passwordEncoder.encode("123456"))
                    .usuario(usuario)
                    .roles(roleEntities)
                    .build();

            credentialsEntity = credentialsRepository.save(credentialsEntity);

            usuario.setCredencial(credentialsEntity);
            usuarioRepository.save(usuario);
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
                            .puntuacionU(8.9)
                            .comentario("Excelente historia y actuaciones.")
                            .fecha(LocalDateTime.now())
                            .build()
            ));
        }
    }


    public void initPlan() {
        if (planRepository.count() == 0) {
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.MENSUAL, 3000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.SEMESTRAL, 15000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.ANUAL, 25000, null));
        }
    }


    public void initCredenciales() {
        createRoleIfNotExists(Role.ROLE_ADMIN);
        createRoleIfNotExists(Role.ROLE_PREMIUM);
        createRoleIfNotExists(Role.ROLE_USER);
    }

    private RoleEntity createRoleIfNotExists(Role role) {
        return roleRepository.findByRole(role).orElseGet(() -> {
            RoleEntity newRole = new RoleEntity();
            newRole.setRole(role);
            return roleRepository.save(newRole);
        });
    }


    private boolean checkPeliBDD(String imdbId) {
        return peliculaRepository.findAll().stream()
                .anyMatch(p -> p.getImdbId().equals(imdbId));
    }

    public boolean checkSerieBDD(String imdbId) {
        return serieRepository.findAll().stream()
                .anyMatch(s -> s.getImdbId().equals(imdbId));
    }


    public void traerSeriesAPI() {
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

    public double calcularPromedioSerie(SerieEntity serie) {
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


    public void traerPeliculaAPI() {
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

    public double calcularPromedioPelicula(PeliculaEntity pelicula) {
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
        return Math.round(promedio * 100.0) / 100.0;
    }

    //tendria que crear una clase de tareas diarias???????
    //todos los dias a las 12pm
    @Scheduled(cron = "0 0 12 * *  ?")
    public void avisarVencimiento(){

        String msj = "Buenas Tardes, le comunicamos que su suscripcion a Movie-Match esta proxima a vencer, si quiere seguir disfrutando del premiun, recuerte renovar" +
                "\n\n Atte. Movie-Match";
        String subject = "Suscripcion proxima a vencer";

        List<SuscripcionEntity> userVencimiento = suscripcionService.avisarVencimiento();

        userVencimiento.forEach(suscripcion -> emailService.sendEmail(suscripcion.getUsuario().getEmail(), subject, msj));

    }

    //bajamos todas las subs vencidas y cambiamos rol
    @Scheduled(cron = "0 0 12 * *  ?")
    public void bajarSubs(){
        suscripcionService.desactivarSuscripion();
    }


}


