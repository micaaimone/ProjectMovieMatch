package com.example.demo.model.DTOs.Resenia;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaMostrarUsuarioDTO {


    @Schema(description = "Nombre del usuario que realizó la reseña", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Puntuación que el usuario asignó al contenido", example = "9.0")
    private Double puntuacionU;

    @Schema(description = "Comentario del usuario sobre el contenido", example = "Muy recomendable, gran desarrollo de personajes.")
    private String comentario;
}
