package com.example.demo.model.entities.User;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.example.demo.model.enums.Genero;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private int edad;

    @Email
    @Column
    private String email;

    @Column
    private String telefono;

    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private CredentialsEntity credencial;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Genero> generos;

    @OneToOne
    @JoinColumn(name = "id_suscripcion")
    private SuscripcionEntity suscripcion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContenidoLikeEntity> contenidoLikes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReseniaLikeEntity> reseniaLikes = new ArrayList<>();

    // Likes a contenidos
    @ManyToMany
    @JoinTable(
            name = "amistades_contenido",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "contenido_id")
    )
    private Set<ContenidoEntity> likes;

    // Amigos (usuarios)
    @ManyToMany
    @JoinTable(
            name = "amistades_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private List<UsuarioEntity> amigos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReseniaEntity> reseñasHechas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ListasContenidoEntity> listas = new ArrayList<>();

    @ManyToMany(mappedBy = "listaUsuarios", fetch = FetchType.LAZY)
    private Set<GrupoEntity> grupos = new HashSet<>();
}
