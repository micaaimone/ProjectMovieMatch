package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.Subs.OfertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {
    private final OfertaService ofertaService;

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }


    @Operation(
            summary = "Crear oferta por tipo de plan",
            description = "Crea una oferta enviando un DTO y un tipo de plan"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta creada correctamente")
    })

    @PreAuthorize("hasAuthority('OFERTA_CREAR')")
    @PostMapping("/crear/{tipoPlan}")
    public ResponseEntity<String> crear(
            @Valid @RequestBody OfertaDTO ofertaDTO,
            @PathVariable("tipoPlan") TipoSuscripcion tipoPlan) {
        ofertaService.CrearOferta(ofertaDTO, tipoPlan);
        return ResponseEntity.ok("Oferta creada correctamente");
    }

    @Operation(
            summary = "Renovar una oferta existente",
            description = "Renueva una oferta existente mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta renovada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Oferta no encontrada", content = @Content)
    })

    @PreAuthorize("hasAuthority('OFERTA_RENOVAR')")
    @PatchMapping("/renovar/{id}")
    public ResponseEntity<String> renovar(@PathVariable Long id) {
        ofertaService.renovarOferta(id);
        return ResponseEntity.ok("Oferta renovada con exito");
    }

    @Operation(
            summary = "Ver todas las ofertas",
            description = "Devuelve una lista paginada de todas las ofertas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ofertas")
    })

    @PreAuthorize("hasAuthority('OFERTA_VER_TODAS')")
    @GetMapping("/verTodas")
    public ResponseEntity<Page<OfertaDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findAll(pageable));
    }

    @Operation(
            summary = "Ver ofertas activas",
            description = "Devuelve una lista paginada de ofertas que están actualmente activas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ofertas activas")
    })

    @PreAuthorize("hasAuthority('OFERTA_VER_ACTIVAS')")
    @GetMapping("/verActivas")
    public ResponseEntity<Page<OfertaDTO>> findActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findActivos(pageable));
    }
}
