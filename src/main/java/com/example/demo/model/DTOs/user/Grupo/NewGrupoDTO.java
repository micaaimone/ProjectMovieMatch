package com.example.demo.model.DTOs.user.Grupo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para crear un nuevo grupo de usuarios")
public class NewGrupoDTO {

    @Schema(
            description = "Nombre del grupo",
            example = "Grupo Cine",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "El nombre del grupo es necesario")
    private String nombre;

    @Schema(
            description = "Descripción opcional del grupo",
            example = "Grupo para charlar de cine"
    )
    private String descripcion;

    @Schema(
            description = "IDs de los usuarios que formarán parte del grupo (al menos 2 integrantes además del creador)",
            example = "[3, 4]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Debe ingresar al menos dos integrantes mas")
    @Size(min = 2, message = "Debes ingresar al menos 2 integrantes mas.")
    private Set<Long> idUsuarios;
}
