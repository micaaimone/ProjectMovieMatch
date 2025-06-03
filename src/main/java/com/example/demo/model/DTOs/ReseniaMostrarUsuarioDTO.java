package com.example.demo.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaMostrarUsuarioDTO {
    @NotBlank(message = "id de usuario es necesario")
    private Long id_contenido;

    private Double puntuacionU;

    private String comentario;
}
