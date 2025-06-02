package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.services.Subs.OfertaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {
    private final OfertaService ofertaService;

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Void> crear(@RequestParam String descripcion, @RequestParam float desc, @RequestParam Long id) {
        ofertaService.CrearOferta(descripcion, desc, id);
    return ResponseEntity.ok().build();
    }

    @PostMapping("/renovar/{id}")
    public ResponseEntity<Void> renovar(@PathVariable Long id) {
        ofertaService.renovarOferta(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verTodas")
    public ResponseEntity<Page<OfertaDTO>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findAll(pageable));
    }

    @GetMapping("/verActivas")
    public ResponseEntity<Page<OfertaDTO>> findActivos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findActivos(pageable));
    }
}
