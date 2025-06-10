package com.example.demo.model.DTOs.Contenido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PeliculaDTO extends ContenidoDTO {

    @Schema(description = "Puntuación Metascore del contenido", example = "82")
    private String metascore;

}
