package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.services.Contenido.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/contenido")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }


    @GetMapping
    public ResponseEntity<Page<ContenidoDTO>> allActivos(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.buscarActivos(pageable, genero, anio, titulo, puntuacion, 0, clasificacion, id));
    }

    @GetMapping("/bajados")
    public ResponseEntity<Page<ContenidoDTO>> allDesactivoados(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.buscarActivos(pageable, genero, anio, titulo, puntuacion, 1, clasificacion, id));
    }


    @PostMapping("/{id}")
    //pongo ? porque puede devolver un contenido dto, como un error detalle
    public ResponseEntity<String> darDeAlta(@PathVariable Long id) {
        contenidoService.darDeAltaBDD(id);
        return ResponseEntity.ok("Contenido dado de alta correctamente.");
    }

    @DeleteMapping("/{id}")
    //pongo ? porque puede devolver un contenido dto, como un error detalle
    public ResponseEntity<String> borrarContenido(@PathVariable long id){
        contenidoService.borrarDeBDD(id);
        return ResponseEntity.ok("Contenido eliminado correctamente.");
    }



}