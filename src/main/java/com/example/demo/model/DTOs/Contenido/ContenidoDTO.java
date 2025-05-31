package com.example.demo.model.DTOs.Contenido;

import com.example.demo.model.DTOs.ReseniaDTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContenidoDTO {
    private String titulo;
    private String anio;
    private String clasificacion;
    private String duracion;
    private String genero;
    private String actores;
    private String sinopsis;
    //lo traemos porue nos sirve para la interfaz en un futuro
    //private String poster;

    //lo comento porque no creo q al usuario le interese de donde sale la puntuacion api
//    private List<RatingDTO> ratings;
    private double puntuacionApi;
    //hace falta crear un atributo para puntuacion de nuestros usuarios

    private List<ReseniaDTO> reseña;

    private double promedioPuntuacionUsuario;

    //importan los votos de la puntuacion api???
//    private String imdbVotos;
}
