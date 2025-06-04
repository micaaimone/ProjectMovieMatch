package com.example.demo.model.entities;


import java.util.HashSet;
import java.util.Set;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;


@Getter
@Setter
@Builder
@AllArgsConstructor
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



    @Column
    private int edad;

    @Column
    private long telefono;



    @Column(name = "username", unique = true)
    private String username;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToOne(mappedBy = "usuario")
    private CredentialsEntity credencial;

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
