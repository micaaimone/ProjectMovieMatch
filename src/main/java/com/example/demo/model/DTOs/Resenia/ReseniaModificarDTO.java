package com.example.demo.model.DTOs.Resenia;

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
    private Double puntuacionU;  // Cambiado a Double para que pueda validarse NotNull

    @Size(min = 1, max = 500, message = "El comentario debe tener entre 1 y 500 caracteres")
    private String comentario;
}
