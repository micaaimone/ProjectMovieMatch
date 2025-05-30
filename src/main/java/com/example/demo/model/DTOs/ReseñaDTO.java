package com.example.demo.model.DTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReseñaDTO {
    private double puntuacionU;
    private String comentario;
    private LocalDateTime fecha;
}
