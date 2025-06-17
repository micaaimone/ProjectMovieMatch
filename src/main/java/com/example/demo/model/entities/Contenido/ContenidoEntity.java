package com.example.demo.model.entities.Contenido;

import com.example.demo.model.entities.User.ContenidoLikeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "contenido")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ContenidoEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_contenido;

    private boolean activo = true;

    @JsonProperty("Title")
    private String titulo;

    @JsonProperty("Year")
    private String anio;

    @JsonProperty("Rated")
    private String clasificacion;

    @JsonProperty("Released")
    private String lanzamiento;

    @JsonProperty("Runtime")
    private String duracion;

    @JsonProperty("Genre")
    private String genero;

    @JsonProperty("Writer")
    private String guionista;

    @JsonProperty("Actors")
    private String actores;

    @JsonProperty("Plot")
    private String sinopsis;

    @JsonProperty("Language")
    private String idioma;

    @JsonProperty("Country")
    private String pais;

    @JsonProperty("Awards")
    private String premios;

    @JsonProperty("Poster")
    private String poster;

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    //usamos eager porque en las consultas principales se requiere de la vista de ratings
    //eager hace q cuando se consulta a la entidad principal (contenidos) esta tabla tmbn cargue automaticamente
    @JsonProperty("Ratings")
    private List<RatingEntity> ratings;

    //esto es de la api, lo mostramos como reseñas generales
    private double puntuacion;

    //aca van nuestras puntuacion, se muestran como reseñas recientes
    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReseniaEntity> reseña;

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("imdbVotes")
    private String imdbVotos;

    @JsonProperty("imdbID")
    @Column(unique = true)
    private String imdbId;

    @JsonProperty("Type")
    private String tipo;

    @JsonProperty("Response")
    private String respuesta;

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL)
    private List<ContenidoLikeEntity> likes = new ArrayList<>();

    public boolean isActivo() {
        return activo;
    }
}