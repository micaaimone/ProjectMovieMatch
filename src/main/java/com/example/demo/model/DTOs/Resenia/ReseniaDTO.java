package com.example.demo.model.DTOs.Resenia;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ReseniaDTO {

    @NotNull(message = "id de usuario es necesario")
    private Long id_usuario;

    @NotNull(message = "id de contenido es necesario")
    private Long id_contenido;

    @Min(value = 0, message = "La puntuacion no puede ser menor a 0")
    @Max(value = 10, message = "La puntuacion no puede ser mayor a 10")
    @NotNull(message = "es necesario que se ingrese una puntuacion")
    private Double puntuacionU;  // Cambiado a Double para que pueda validarse NotNull

    @Size(min = 1, max = 500, message = "El comentario debe tener entre 1 y 500 caracteres")
    private String comentario;
}


