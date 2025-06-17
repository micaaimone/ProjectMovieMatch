package com.example.demo.model.DTOs.user.Grupo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "DTO para modificar el nombre o la descripción de un grupo existente")
public class ModificarGrupoDTO {

    @Schema(
            description = "Nuevo nombre del grupo",
            example = "Grupo Cine Actualizado",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "El nombre del grupo es necesario")
    private String nombre;

    @Schema(
            description = "Nueva descripción del grupo (opcional)",
            example = "Grupo actualizado para debatir películas modernas"
    )
    private String descripcion;
}
