package com.example.demo.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
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

    @Size(min = 6, max = 30)
    @Column(nullable = false, length = 30)
    private String contrasenia;

    @Size(min = 4)
    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToOne
    @JoinColumn(name = "id_credencial")
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


}
