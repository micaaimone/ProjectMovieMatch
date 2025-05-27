package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.services.Contenido.PeliculaService;
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


    @GetMapping("/todasLasPelis")
    public ResponseEntity<Page<PeliculaDTO>> listarPeliculas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(peliculaService.datosBDD(pageable));
    }

    @GetMapping("/buscarByID/{id}")
    public ResponseEntity<PeliculaDTO> findByID(@PathVariable Long id)
    {
        return ResponseEntity.ok(peliculaService.buscarByID(id));
    }

    @GetMapping("/puntuacion")
    public ResponseEntity<Page<PeliculaDTO>> byPuntuacion(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(peliculaService.filtrarPorPuntuacion(pageable));
    }
}
