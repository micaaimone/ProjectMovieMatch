package com.example.demo.model.DTOs.Resenia;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ReseniaDTO {
    private Long id;
    private Long id_usuario;

    private String username;

    @NotNull(message = "id de contenido es necesario")
    @Schema(description = "ID del contenido al que pertenece la reseña", example = "12")
    private Long id_contenido;

    private String titulo;

    @Min(value = 0, message = "La puntuacion no puede ser menor a 0")
    @Max(value = 10, message = "La puntuacion no puede ser mayor a 10")
    @NotNull(message = "es necesario que se ingrese una puntuacion")
    @Schema(description = "Puntuación del usuario al contenido, entre 0 y 10", example = "8.5")
    private Double puntuacionU;

    @Size(min = 1, max = 500, message = "El comentario debe tener entre 1 y 500 caracteres")
    @Schema(description = "Comentario del usuario sobre el contenido (opcional)", example = "Una película emocionante y con giros inesperados.")
    private String comentario;
}


