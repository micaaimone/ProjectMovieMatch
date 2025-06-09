package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.services.Contenido.SerieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/series")
public class SerieController {
    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @Operation(
            summary = "Obtener series filtradas",
            description = "Devuelve una lista paginada de series filtradas por género, año, título, puntuación, estado, clasificación y temporadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de series obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    // 🔐 SOLO LECTURA, ACCESIBLE PARA TODOS LOS ROLES
    @PreAuthorize("hasAuthority('SERIES_READ')")
    @GetMapping
    public ResponseEntity<Page<SerieDTO>> allSeriesActivas(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) String temporadas,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(serieService.buscar(pageable, genero, anio, titulo, puntuacion, 0, clasificacion, temporadas, id));
    }




}
