package com.example.demo.model.DTOs.Resenia;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaMostrarUsuarioDTO {

    private String nombre;

    private Double puntuacionU;

    private String comentario;
}
