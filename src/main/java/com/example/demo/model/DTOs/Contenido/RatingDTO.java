package com.example.demo.model.DTOs.Contenido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RatingDTO {
    @Schema(description = "Fuente de la puntuación", example = "Internet Movie Database")
    private String fuente;

    @Schema(description = "Valor de la puntuación según la fuente", example = "8.2/10")
    private String valor;
}

//