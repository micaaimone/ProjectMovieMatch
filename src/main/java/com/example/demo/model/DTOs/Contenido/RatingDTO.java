package com.example.demo.model.DTOs.Contenido;

import com.example.demo.model.entities.Contenido.RatingEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RatingDTO {
    private String fuente;
    private String valor;
}