package com.example.demo.model.DTOs.user.Grupo;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ModificarGrupoDTO {
    @NotEmpty(message = "El nombre del grupo es necesario")
    private String nombre;

    private String descripcion;
}
