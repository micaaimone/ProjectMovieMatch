package com.example.demo.model.DTOs.user.Grupo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewGrupoDTO {
    @NotEmpty(message = "El nombre del grupo es necesario")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debe ingresar al menos dos integrantes mas")
    @Size(min = 2, message = "Debes ingresar al menos 2 integrantes mas.")
    private Set<Long> idUsuarios;
}
