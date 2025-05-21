package com.example.demo.model.DTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContenidoDTO {
    private Long id_contenido;
    private String titulo;
    private String anio;
    private String clasificacion;
    private String duracion;
    private String genero;
    private String actores;
    private String sinopsis;
    //lo traemos porue nos sirve para la interfaz en un futuro
    //private String poster;
    private List<RatingDTO> ratings;
    private String imdbRating;
    private String imdbVotos;
}
