package com.example.demo.model.DTOs.Resenia;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaModificarDTO {

    @Min(value = 0, message = "La puntuacion no puede ser menor a 0")
    @Max(value = 10, message = "La puntuacion no puede ser mayor a 10")
    @NotNull(message = "es necesario que se ingrese una puntuacion")
    @Schema(description = "Nueva puntuación asignada por el usuario al contenido", example = "7.5")
    private Double puntuacionU;

    @Size(min = 1, max = 500, message = "El comentario debe tener entre 1 y 500 caracteres")
    @Schema(description = "Comentario actualizado del usuario sobre el contenido", example = "Mejoró en el segundo acto, pero el final fue flojo.")
    private String comentario;
}
