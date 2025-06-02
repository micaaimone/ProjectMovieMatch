package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.services.Subs.PagoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/mostrarTodos")
    public Page<PagoDTO> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return pagoService.findAll(pageable);
    }

    @GetMapping("/mostrarXsub/{id}")
    public Page<PagoDTO> findBySubId(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return pagoService.findBySub(id, pageable);
    }

    @GetMapping("/mostrar/{id}")
    public ResponseEntity<PagoDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(pagoService.findById(id));
    }
}
