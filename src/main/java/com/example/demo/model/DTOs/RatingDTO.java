package com.example.demo.model.DTOs;

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