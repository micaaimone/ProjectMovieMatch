package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
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
    public ResponseEntity<Page<SerieDTO>> listarSeries(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(serieService.datosBDD(pageable));
    }

    @GetMapping("/buscarByID/{id}")
    public ResponseEntity<SerieDTO> findByID(@PathVariable Long id)
    {
        return ResponseEntity.ok(serieService.buscarByID(id));
    }

    @GetMapping("/puntuacion")
    public ResponseEntity<Page<SerieDTO>> byPuntuacion(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(serieService.filtrarPorPuntuacion(pageable));
    }
}
