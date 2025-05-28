package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.services.Contenido.SerieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/series")
public class SerieController {
    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public ResponseEntity<Page<SerieDTO>> all(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) String temporadas,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(serieService.buscar(pageable, genero, anio, titulo, puntuacion, estado, clasificacion, temporadas));
    }


}
