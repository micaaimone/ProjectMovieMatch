package com.example.demo.model.DTOs.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContenidoDTO {

    private Long id;

    private boolean activo;

    @Schema(description = "Título del contenido", example = "Breaking Bad")
    private String titulo;

    @Schema(description = "Año de lanzamiento", example = "2008")
    private String anio;

    @Schema(description = "Clasificación por edad", example = "TV-MA")
    private String clasificacion;

    @Schema(description = "Duración del contenido (en minutos o formato hh:mm)", example = "47m")
    private String duracion;

    @Schema(description = "Género principal del contenido", example = "Drama, Crimen")
    private String genero;

    @Schema(description = "Actores principales", example = "Bryan Cranston, Aaron Paul")
    private String actores;

    @Schema(description = "Sinopsis del contenido", example = "Un profesor de química con cáncer terminal comienza a fabricar metanfetamina.")
    private String sinopsis;

    // lo traemos porque nos sirve para la interfaz en un futuro
    // private String poster;

    @Schema(description = "Puntuación de una API externa (por ejemplo, IMDb)", example = "8.9")
    private double puntuacionApi;

    // hace falta crear un atributo para puntuación de nuestros usuarios
    @Schema(description = "Lista de reseñas hechas por los usuarios")
    private List<ReseniaDTO> reseña;

    @Schema(description = "Promedio de puntuaciones dadas por los usuarios registrados", example = "9.1")
    private double promedioPuntuacionUsuario;

}
