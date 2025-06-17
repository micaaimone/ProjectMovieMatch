package com.example.demo.model.DTOs.Contenido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SerieDTO extends ContenidoDTO {

    @Schema(description = "Cantidad de temporadas de la serie", example = "5")
    private String temporadas;

}