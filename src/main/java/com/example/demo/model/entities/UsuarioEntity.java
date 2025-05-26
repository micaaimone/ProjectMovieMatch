package com.example.demo.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Set;

//@Getter
//@Setter
//@ToString
//@RequiredArgsConstructor
//@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "id_credencial", nullable = false)
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

    public UsuarioEntity(Long id, String nombre, String apellido, String email, int edad, long telefono, String contrasenia, String username, boolean activo, CredencialEntity credencial) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
        this.username = username;
        this.activo = activo;
        this.credencial = credencial;
        this.likes = likes;
    }

    public UsuarioEntity() {
    }

    // GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public @Size(min = 6, max = 30) String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@Size(min = 6, max = 30) String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public @Size(min = 4) String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 4) String username) {
        this.username = username;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public CredencialEntity getCredencial() {
        return credencial;
    }

    public void setCredencial(CredencialEntity credencial) {
        this.credencial = credencial;
    }

    public Set<ContenidoEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<ContenidoEntity> likes) {
        this.likes = likes;
    }
}
