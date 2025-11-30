package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.ResponseDTO;
import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.Subs.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/planes")
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(
            summary = "Listar planes",
            description = "Devuelve una lista paginada de todos los planes de suscripción disponibles."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado de planes")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('PLAN_VER')")
    @GetMapping("/ver")
    public Page<PlanDTO> mostrarPlanes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return planService.verTodos(pageable);
    }

    @Operation(
            summary = "Actualizar precio de un plan",
            description = "Actualiza el monto de un plan de suscripción existente según su tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado", content = @Content)
    })
    @Parameters({
            @Parameter(
                    name = "tipoPlan",
                    description = "Tipo de suscripción del plan a actualizar",
                    required = true,
                    schema = @Schema(implementation = TipoSuscripcion.class)
            ),
            @Parameter(
                    name = "precio",
                    description = "Nuevo precio del plan",
                    required = true,
                    schema = @Schema(type = "number", format = "float", example = "1499.99")
            )
    })
    @PreAuthorize("hasAuthority('PLAN_EDITAR')")
    @PatchMapping("/{tipoPlan}/cambiarPrecio")
    public ResponseEntity<ResponseDTO> actualizarPlan(
            @PathVariable("tipoPlan") TipoSuscripcion tipoPlan,
            @RequestParam BigDecimal precio
    ) {
        planService.cambiarMontoPlan(tipoPlan, precio);
        return ResponseEntity.ok(new ResponseDTO("Plan actualizado correctamente"));
    }
}
