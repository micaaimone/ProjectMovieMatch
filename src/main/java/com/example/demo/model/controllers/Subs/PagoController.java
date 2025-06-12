package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.services.Subs.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(
            summary = "Mostrar todos los pagos",
            description = "Devuelve una lista paginada de todos los pagos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado paginado de pagos")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('PAGO_VER_TODOS')")
    @GetMapping("/mostrarTodos")
    public Page<PagoDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return pagoService.findAll(pageable);
    }

    @Operation(
            summary = "Mostrar pagos por suscripción",
            description = "Devuelve una lista paginada de pagos filtrados por el ID de una suscripción específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado paginado de pagos por suscripción"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @Parameters({
            @Parameter(name = "id", description = "ID de la suscripción", required = true, schema = @Schema(type = "integer", example = "3")),
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('PAGO_VER_POR_SUSCRIPCION')")
    @GetMapping("/mostrarXsub/{id}")
    public Page<PagoDTO> findBySubId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return pagoService.findBySub(id, pageable);
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene la información detallada de un pago específico mediante su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado", content = @Content)
    })
    @Parameter(name = "id", description = "ID del pago", required = true, schema = @Schema(type = "integer", example = "12"))
    @PreAuthorize("hasAuthority('PAGO_VER_POR_ID')")
    @GetMapping("/mostrar/{id}")
    public ResponseEntity<PagoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }
}
