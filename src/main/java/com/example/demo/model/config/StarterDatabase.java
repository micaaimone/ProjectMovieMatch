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
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
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
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Configuration;
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
    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReseniaRepository reseñaRepository;
    private final ContenidoRepository contenidoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;

    public StarterDatabase(APIMovieService apiMovieService, RatingRepository ratingRepository, PeliculaRepository peliculaRepository, SerieRepository serieRepository, PlanRepository planRepository, SuscripcionRepository suscripcionRepository, UsuarioRepository usuarioRepository, ReseniaRepository reseñaRepository, ContenidoRepository contenidoRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.apiMovieService = apiMovieService;
        this.ratingRepository = ratingRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.reseñaRepository = reseñaRepository;
        this.contenidoRepository = contenidoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
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
        validarSubs();
        initCredenciales();
        initUsers();
        initReseñas();

    }


    public void initUsers() {
        crearUsuarioSiNoExiste("lautaM", "Lautaro", "Martínez", 28, "1123456789", "rama@gmail.com");
        crearUsuarioSiNoExiste("meliR", "Melina", "Rodríguez", 25, "1123456790", "meli@gmail.com");
        crearUsuarioSiNoExiste("tomiG", "Tomás", "Gómez", 30, "1123456791", "tomi@gmail.com");
        crearUsuarioSiNoExiste("sofiP", "Sofía", "Pérez", 27, "1123456792", "sofi@gmail.com");
    }

    private void crearUsuarioSiNoExiste(String username, String nombre, String apellido, int edad, String telefono, String email) {
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
                    .activo(true)
                    .build();

            usuario = usuarioRepository.save(usuario);
        }

        RoleEntity roleEntity = roleRepository.findByRole(Role.ROLE_ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Rol inexistente " + Role.ROLE_ADMIN));
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


//    public void initUsers() {
//        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByUsername("lautaM");
//
//        UsuarioEntity usuario;
//
//        if (usuarioOptional.isPresent()) {
//            usuario = usuarioOptional.get();
//        } else {
//            usuario = UsuarioEntity.builder()
//                    .nombre("Lautaro")
//                    .apellido("Martínez")
//                    .edad(28)
//                    .telefono("1123456789")
//                    .username("lautaM")
//                    .activo(true)
//                    .build();
//
//            usuario = usuarioRepository.save(usuario);
//        }
//
//        RoleEntity roleEntity = roleRepository.findByRole(Role.ROLE_ADMIN)
//                .orElseThrow(() -> new EntityNotFoundException("Rol inexsistente" + Role.ROLE_ADMIN));
//        HashSet<RoleEntity> roleEntities = new HashSet<>();
//        roleEntities.add(roleEntity);
//        // Verificamos si ya tiene credencial
//        if (usuario.getCredencial() == null) {
//            CredentialsEntity credentialsEntity = CredentialsEntity.builder()
//                    .email("rama@gmail.com")
//                    .password(passwordEncoder.encode("123456"))
//                    .usuario(usuario)
//                    .roles(roleEntities)
//                    .build();
//
//            credentialsEntity = credentialsRepository.save(credentialsEntity);
//
//            usuario.setCredencial(credentialsEntity);
//            usuarioRepository.save(usuario);
//        }
//
//    }

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

    public void validarSubs() {
        suscripcionRepository.VerificarSuscripcion(LocalDate.now());
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
}


