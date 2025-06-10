package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.Subs.OfertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            description = "Crea una nueva oferta en el sistema asociada a un tipo de suscripción específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al crear la oferta", content = @Content)
    })
    @PreAuthorize("hasAuthority('OFERTA_CREAR')")
    @PostMapping("/crear/{tipoPlan}")
    public ResponseEntity<String> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la oferta a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OfertaDTO.class))
            )
            @Valid @RequestBody OfertaDTO ofertaDTO,

            @Parameter(
                    description = "Tipo de suscripción al que se asigna la oferta",
                    required = true,
                    schema = @Schema(implementation = TipoSuscripcion.class)
            )
            @PathVariable("tipoPlan") TipoSuscripcion tipoPlan
    ) {
        ofertaService.CrearOferta(ofertaDTO, tipoPlan);
        return ResponseEntity.ok("Oferta creada correctamente");
    }

    @Operation(
            summary = "Renovar una oferta existente",
            description = "Renueva una oferta previamente creada, identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oferta renovada con éxito"),
            @ApiResponse(responseCode = "404", description = "Oferta no encontrada", content = @Content)
    })
    @PreAuthorize("hasAuthority('OFERTA_RENOVAR')")
    @PatchMapping("/renovar/{id}")
    public ResponseEntity<String> renovar(
            @Parameter(
                    description = "ID de la oferta a renovar",
                    required = true,
                    schema = @Schema(type = "integer", example = "1")
            )
            @PathVariable Long id
    ) {
        ofertaService.renovarOferta(id);
        return ResponseEntity.ok("Oferta renovada con exito");
    }

    @Operation(
            summary = "Ver todas las ofertas",
            description = "Devuelve una lista paginada con todas las ofertas del sistema, activas o inactivas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de todas las ofertas")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('OFERTA_VER_TODAS')")
    @GetMapping("/verTodas")
    public ResponseEntity<Page<OfertaDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findAll(pageable));
    }

    @Operation(
            summary = "Ver ofertas activas",
            description = "Devuelve una lista paginada con solo las ofertas activas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de ofertas activas")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('OFERTA_VER_ACTIVAS')")
    @GetMapping("/verActivas")
    public ResponseEntity<Page<OfertaDTO>> findActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.findActivos(pageable));
    }
}
