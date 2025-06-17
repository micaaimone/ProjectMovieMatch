package com.example.demo.model.DTOs.Contenido;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoPageDTO {
    private List<ContenidoDTO> contenidos;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;

}
