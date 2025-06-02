package com.example.demo.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "peliculas")
@PrimaryKeyJoinColumn(name = "pelicula_id")
public class PeliculaEntity extends ContenidoEntity{

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Metascore")
    private String metascore;

    @JsonProperty("DVD")
    private String dvd;

    @JsonProperty("BoxOffice")
    private String recaudacion;

    @JsonProperty("Production")
    private String productora;

    @JsonProperty("Website")
    private String sitioWeb;

}