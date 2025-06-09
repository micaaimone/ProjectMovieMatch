package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.services.Contenido.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;


    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @Operation(
            summary = "Obtener películas filtradas",
            description = "Devuelve una lista paginada de películas filtradas por género, año, título, puntuación, estado, clasificación y metascore. Todos los filtros son opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de películas obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
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

        return ResponseEntity.ok(peliculaService.buscar(pageable, genero, anio, titulo, puntuacion, 0, clasificacion, metascore, id));
    }

}
