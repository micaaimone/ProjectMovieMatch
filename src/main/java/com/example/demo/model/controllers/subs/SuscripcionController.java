package com.example.demo.model.controllers.subs;

import com.example.demo.model.DTOs.subs.SuscripcionDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.subs.MPService;
import com.example.demo.model.services.subs.SuscripcionService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/suscripciones")
public class SuscripcionController {
    private final SuscripcionService suscripcionService;
    private final MPService mpService;

    public SuscripcionController(SuscripcionService suscripcionService, MPService mpService) {
        this.suscripcionService = suscripcionService;
        this.mpService = mpService;
    }


    @PostMapping("/crear")
    public ResponseEntity<String> crearSuscripcion(@RequestParam Long idUsuario, @RequestParam TipoSuscripcion tipo) throws MPException, MPApiException {
       String init = mpService.crearPreferencia(suscripcionService.save(idUsuario, tipo));

        return ResponseEntity.ok(init);
    }

    @GetMapping("/mostrarTodos")
    public Page<SuscripcionDTO> mostrarSuscripciones(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return suscripcionService.findAll(pageable);

    }

    @GetMapping("/mostrarActivos")
    public Page<SuscripcionDTO> mostrarActivos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return suscripcionService.mostrarActivos(pageable);
    }

    @GetMapping("/mostrar/{id}")
    public ResponseEntity<SuscripcionDTO> mostrarSuscripcion(@PathVariable Long id){
        return ResponseEntity.ok(suscripcionService.findById(id));
    }


}
