package com.example.demo.model.DTOs.Contenido;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContenidoMostrarDTO {
    private Long id;

    @Schema(description = "Título del contenido", example = "El Señor de los Anillos")
    private String titulo;

    @Schema(description = "Tipo de contenido (Pelicula o Serie)", example = "Pelicula")
    private String tipo;

    private String poster;
}
