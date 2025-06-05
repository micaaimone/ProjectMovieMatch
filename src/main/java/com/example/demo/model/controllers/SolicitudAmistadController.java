package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.SolicitudAmistadDTO;
import com.example.demo.model.services.SolicitudAmistadService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/amistades")
public class SolicitudAmistadController {
    private final SolicitudAmistadService solicitudAmistadService;

    public SolicitudAmistadController(SolicitudAmistadService solicitudAmistadService) {
        this.solicitudAmistadService = solicitudAmistadService;
    }

    @PostMapping("/enviarSolicitud")
    public ResponseEntity<String> enviarSolicitud(@Valid @RequestBody  NewSolicitudAmistadDTO s)
    {
        solicitudAmistadService.save(s);
        return ResponseEntity.ok("Solicitud enviada con exito.");
    }

    @PostMapping("/{idEmisor}/aceptarSolicitud/{idReceptor}")
    public ResponseEntity<String> aceptarSolicitud(@PathVariable Long idEmisor, @PathVariable Long idReceptor) {
        solicitudAmistadService.aceptarSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud aceptada");
    }

    @PostMapping("/{idEmisor}/rechazarSolicitud/{idReceptor}")
    public ResponseEntity<String> rechazarSolicitud(@PathVariable Long idEmisor, @PathVariable Long idReceptor) {
        solicitudAmistadService.rechazarSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud rechazada");
    }

    @PostMapping("/{idEmisor}/bloquear/{idReceptor}")
    public ResponseEntity<String> bloquear(@PathVariable Long idEmisor, @PathVariable Long idReceptor) {
        solicitudAmistadService.bloquearSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud bloqueada");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarPorID(@PathVariable("id") Long idEmisor,
                                                                 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(solicitudAmistadService.listarMisSolicitudes(idEmisor, pageable));
    }
}
