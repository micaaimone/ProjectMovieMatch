package com.example.demo.model.DTOs.user.Grupo;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VisualizarGrupoDTO {
    private String nombre;

    private String descripcion;

    private UsuarioGrupoDTO admin;

    private Set<UsuarioGrupoDTO> usuarios;
}
