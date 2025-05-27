package com.example.demo.model.entities;


import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

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

    public UsuarioEntity(Long id, String nombre, String apellido, int edad, long telefono, String contrasenia, String username, boolean activo, CredencialEntity credencial) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
        this.username = username;
        this.activo = activo;
        this.credencial = credencial;
        this.likes = new HashSet<>();
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia( String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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
