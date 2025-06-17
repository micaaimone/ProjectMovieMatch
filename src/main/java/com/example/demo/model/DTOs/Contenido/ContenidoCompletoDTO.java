package com.example.demo.model.DTOs.Contenido;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContenidoCompletoDTO {
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

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("imdbVotes")
    private String imdbVotos;

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Type")
    private String tipo;

    @JsonProperty("Response")
    private String respuesta;
}
