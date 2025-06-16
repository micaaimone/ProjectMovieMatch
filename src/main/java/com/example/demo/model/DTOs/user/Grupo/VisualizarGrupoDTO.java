package com.example.demo.model.DTOs.user.Grupo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "DTO para visualizar un grupo, incluyendo su nombre, descripción, administrador y usuarios integrantes")
public class VisualizarGrupoDTO {

    @Schema(
            description = "Nombre del grupo",
            example = "Amigos de Cine",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
            description = "Descripción del grupo",
            example = "Grupo para compartir películas favoritas",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String descripcion;

    @Schema(
            description = "Administrador del grupo"
    )
    private UsuarioGrupoDTO admin;

    @Schema(
            description = "Conjunto de usuarios que pertenecen al grupo"
    )
    private Set<UsuarioGrupoDTO> usuarios;
}
