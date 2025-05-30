package com.example.demo.model.entities;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.subs.ListasContenidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;


@Getter
@Setter

@NoArgsConstructor
@ToString
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
    private boolean activo = true;

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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ListasContenidoEntity> listasContenido;

    public UsuarioEntity(String nombre, String apellido, String email, int edad, long telefono, String contrasenia, String username, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
        this.username = username;
        this.activo = activo;
    }
}
