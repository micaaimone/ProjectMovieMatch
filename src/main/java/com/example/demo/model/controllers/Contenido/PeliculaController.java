package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.services.Contenido.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    @Autowired
    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @PreAuthorize("hasAuthority('VER_PELICULAS')")
    @Operation(
            summary = "Obtener películas filtradas",
            description = """
        Devuelve una lista paginada de películas activas filtradas por:
        - Género
        - Año
        - Título parcial
        - Puntuación mínima
        - Clasificación por edad
        - Metascore
        - ID específico

        Todos los parámetros son opcionales.
    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de películas obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameters({
            @Parameter(name = "genero", description = "Género de la película", schema = @Schema(type = "string", example = "Aventura")),
            @Parameter(name = "anio", description = "Año de estreno", schema = @Schema(type = "string", example = "2022")),
            @Parameter(name = "titulo", description = "Parte del título de la película", schema = @Schema(type = "string", example = "Spider-Man")),
            @Parameter(name = "puntuacion", description = "Puntuación mínima", schema = @Schema(type = "number", format = "double", example = "7.5")),
            @Parameter(name = "clasificacion", description = "Clasificación por edad (ej: PG, R)", schema = @Schema(type = "string", example = "PG")),
            @Parameter(name = "metascore", description = "Metascore exacto o mínimo", schema = @Schema(type = "string", example = "70")),
            @Parameter(name = "id", description = "ID único de la película", schema = @Schema(type = "integer", format = "int64", example = "14")),
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "10"))
    })
    @GetMapping
    public ResponseEntity<Page<PeliculaDTO>> allPelisActivas(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) String metascore,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(peliculaService.buscar(pageable, genero, anio, titulo, puntuacion, true, clasificacion, metascore, id));
    }

}
