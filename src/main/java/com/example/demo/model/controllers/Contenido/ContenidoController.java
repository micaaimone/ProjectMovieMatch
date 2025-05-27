package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.services.Contenido.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/contenido")
public class ContenidoController {

    private final ContenidoService contenidoService;
    private final PagedResourcesAssembler<ContenidoDTO> pagedResourcesAssambler;

    @Autowired
    public ContenidoController(ContenidoService contenidoService, PagedResourcesAssembler<ContenidoDTO> pagedResourcesAssambler) {
        this.contenidoService = contenidoService;
        this.pagedResourcesAssambler = pagedResourcesAssambler;
    }

    @GetMapping("/verSeriesYPeliculas")
    public ResponseEntity<Page<ContenidoDTO>> listado(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.datosBDD(pageable));
    }

    @GetMapping("/verBajasLogicas")
    public ResponseEntity<Page<ContenidoDTO>> listadoBajaLogica(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.datosDadosDeBajaBDD(pageable));
    }

    @GetMapping("/buscarByID/{id}")
    public ResponseEntity<ContenidoDTO> buscarID(@PathVariable Long id)
    {
        return ResponseEntity.ok( contenidoService.buscarByID(id));
    }

    //no funca
//    @GetMapping("allByGenero/{genero}")
//    public ResponseEntity<Page<ContenidoDTO>> byGenero(@PathVariable String genero, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
//    {
//        Pageable pageable = PageRequest.of(page, size);
//
//        return ResponseEntity.ok(contenidoService.buscarPorGenero(genero, pageable));
//    }

    //x2
//    @GetMapping("/allByAnio/{anio}")
//    public ResponseEntity<Page<ContenidoDTO>> byAnio(@PathVariable int anio, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
//    {
//        Pageable pageable = PageRequest.of(page, size);
//
//        return ResponseEntity.ok(contenidoService.filtrarPorAnio(anio, pageable));
//    }

    @GetMapping("/tituloParcial/{titulo}")
    public ResponseEntity<Page<ContenidoDTO>> byTitulo(@PathVariable String titulo, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.filtrarPorTituloParcial(titulo,pageable));
    }


    @PostMapping("/{id}")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        if (contenidoService.darDeAltaBDD(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        if (contenidoService.borrarDeBDD(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}