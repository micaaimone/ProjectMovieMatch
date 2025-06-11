package com.example.demo.model.entities.User;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import com.example.demo.model.enums.Genero;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
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

    @Email
    @Column
    private String email;

    @Column
    private int edad;

    @Column
    private String telefono;

    @Column(nullable = false, length = 30)
    private String contrasenia;

    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private Boolean activo = true;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Genero> generos;

    @ManyToOne
    @JoinColumn(name = "id_credencial", referencedColumnName = "id")
    private CredencialEntity credencial;

    @OneToOne
    @JoinColumn(name = "id_suscripcion")
    private SuscripcionEntity suscripcion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContenidoLike> contenidoLikes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReseniaLike> reseniaLikes = new ArrayList<>();
    //tabla intermedia de amigos
    @ManyToMany
    @JoinTable(
            name = "amistades",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private List<UsuarioEntity> amigos;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReseniaEntity> reseñasHechas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ListasContenidoEntity> listas;


}
