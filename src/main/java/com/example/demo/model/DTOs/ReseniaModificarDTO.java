package com.example.demo.model.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaModificarDTO {
    private Double puntuacionU;
    private String comentario;
}
