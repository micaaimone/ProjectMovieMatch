package com.example.demo.model.entities;


import java.util.List;
import java.util.Set;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;


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
    private long telefono;

    @Column(nullable = false, length = 30)
    private String contrasenia;

    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_credencial", referencedColumnName = "id")
    private CredencialEntity credencial;

//    @OneToOne
//    @JoinColumn(name = "id_suscripcion")
//    private SuscripcionEntity suscripcion;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_contenido")
    )
    private Set<ContenidoEntity> likes;


    //vamos a crear una entidad amigos, va a tener
    //id
    //id_usuario (base)
    //lista de ids

    //tmbn vamos a tener una clase de solicitud amistad
    //id
    //id_usuario(propio)
    //id_usuario (q te manda soli)
    //boolean si o no

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReseniaEntity> reseñasHechas;
}
