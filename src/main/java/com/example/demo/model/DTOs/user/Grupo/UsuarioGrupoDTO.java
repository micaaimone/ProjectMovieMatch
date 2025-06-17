package com.example.demo.model.DTOs.user.Grupo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "DTO con la información básica de un usuario dentro de un grupo")
public class UsuarioGrupoDTO {

    @Schema(
            description = "ID del usuario",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long id;

    @Schema(
            description = "Nombre de usuario",
            example = "juan123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;
}
