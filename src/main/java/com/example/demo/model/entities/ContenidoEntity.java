package com.example.demo.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ContenidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_contenido;

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

    @OneToMany(mappedBy = "contenido", cascade = CascadeType.ALL)
    @JsonProperty("Ratings")
    private List<RatingEntity> ratings;

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

    public String getImdbId() {
        return imdbId;
    }

    public List<RatingEntity> getRatings() {
        return ratings;
    }
}
