package com.example.demo.model.config;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.Seguridad.repositories.RoleRepository;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.RatingEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;

import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;

import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.repositories.Contenido.RatingRepository;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.repositories.Subs.PlanRepository;
import com.example.demo.model.repositories.Subs.SuscripcionRepository;

import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.services.Contenido.APIMovieService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@Configuration
public class StarterDatabase {

    private final APIMovieService apiMovieService;
    private final RatingRepository ratingRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final CredentialsRepository credencialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;


    public StarterDatabase(APIMovieService apiMovieService, RatingRepository ratingRepository, PeliculaRepository peliculaRepository,
                           SerieRepository serieRepository, PlanRepository planRepository, SuscripcionRepository suscripcionRepository, CredentialsRepository credencialRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.apiMovieService = apiMovieService;
        this.ratingRepository = ratingRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.credencialRepository = credencialRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
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
        //initRoles();
        initCredenciales();
        initUsers();

    }

    public void initUsers() {
        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findByUsername("lautaM");

        UsuarioEntity usuario;

        if (usuarioOptional.isPresent()) {
            usuario = usuarioOptional.get();
        } else {
            usuario = UsuarioEntity.builder()
                    .nombre("Lautaro")
                    .apellido("Martínez")
                    .edad(28)
                    .telefono(1123456789L)
                    .username("lautaM")
                    .activo(true)
                    .build();

            usuario = usuarioRepository.save(usuario);
        }

        RoleEntity roleEntity = roleRepository.findByRole(Role.ROLE_ADMIN)
                .orElseThrow(()-> new EntityNotFoundException("Rol inexsistente" +Role.ROLE_ADMIN));
        HashSet<RoleEntity>roleEntities = new HashSet<>();
        roleEntities.add(roleEntity);
        // Verificamos si ya tiene credencial
        if (usuario.getCredencial() == null) {
            CredentialsEntity credentialsEntity = CredentialsEntity.builder()
                    .email("rama@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .usuario(usuario)
                    .roles(roleEntities)
                    .build();

            credentialsEntity = credencialRepository.save(credentialsEntity);

            usuario.setCredencial(credentialsEntity);
            usuarioRepository.save(usuario);
        }

    }


    public void initPlan(){
        if (planRepository.count() == 0) {
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.MENSUAL, 3000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.SEMESTRAL, 15000, null));
            planRepository.save(new PlanSuscripcionEntity(TipoSuscripcion.ANUAL, 25000, null));
        }
    }
/*
    public void initRoles() {
        if (roleRepository.findByRole(Role.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new com.example.demo.Seguridad.Entities.RoleEntity(Role.ROLE_ADMIN));
        }
        if (roleRepository.findByRole(Role.ROLE_PREMIUM).isEmpty()) {
            roleRepository.save(new com.example.demo.Seguridad.Entities.RoleEntity(Role.ROLE_PREMIUM));
        }
        if (roleRepository.findByRole(Role.ROLE_USER).isEmpty()) {
            roleRepository.save(new com.example.demo.Seguridad.Entities.RoleEntity(Role.ROLE_USER));
        }
    }
*/

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

            //seteo que automaticamente el estado sea 0 (activo)
            pelicula.setEstado(0);
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
 /*
        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre("Lautaro")
                .apellido("Martínez")
                .edad(28)
                .telefono(1123456789L)
                .username("lautaM")
                .activo(true)
                .build();

        usuario = usuarioRepository.save(usuario);
        CredentialsEntity credentialsEntity = CredentialsEntity.builder()
                .email("rama@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .usuario(usuario)
                .build();

        credentialsEntity = credencialRepository.save(credentialsEntity);

        usuario.setCredencial(credentialsEntity);
        usuarioRepository.save(usuario);
        /*
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
        }*//*
    public void initCredenciales() {
        // Agarra RoleEntity desde la base de datos
        com.example.demo.Seguridad.Entities.RoleEntity adminRole = roleRepository.findByRole(Role.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("Role ADMIN not found. Ensure roles are initialized."));
        com.example.demo.Seguridad.Entities.RoleEntity premiumRole = roleRepository.findByRole(Role.ROLE_PREMIUM)
                .orElseThrow(() -> new IllegalStateException("Role PREMIUM not found. Ensure roles are initialized."));
        com.example.demo.Seguridad.Entities.RoleEntity userRole = roleRepository.findByRole(Role.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found. Ensure roles are initialized."));

        // Inicializar credencial de administrador
        if (credencialRepository.findByEmail("admin@example.com").isEmpty()) {
            CredentialsEntity adminCredential = CredentialsEntity.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .build();
            credencialRepository.save(adminCredential);
        }

        // Inicializar credencial premium
        if (credencialRepository.findByEmail("premium@example.com").isEmpty()) {
            CredentialsEntity premiumCredential = CredentialsEntity.builder()
                    .email("premium@example.com")
                    .password(passwordEncoder.encode("premium456"))
                    .roles(Set.of(premiumRole))
                    .build();
            credencialRepository.save(premiumCredential);
        }

        // Inicializar credencial de usuario estándar
        if (credencialRepository.findByEmail("user@example.com").isEmpty()) {
            CredentialsEntity userCredential = CredentialsEntity.builder()
                    .email("user@example.com")
                    .password(passwordEncoder.encode("user789"))
                    .roles(Set.of(userRole))
                    .build();
            credencialRepository.save(userCredential);
        }
    }
*/