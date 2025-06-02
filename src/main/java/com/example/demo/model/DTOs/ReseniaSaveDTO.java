package com.example.demo.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaSaveDTO {
    private Long id_usuario;
    private Long id_contenido;
    private double puntuacionU;
    private String comentario;
}
