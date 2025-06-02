package com.example.demo.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaMostrarUsuarioDTO {
    private Long id_contenido;
    private double puntuacionU;
    private String comentario;
}
