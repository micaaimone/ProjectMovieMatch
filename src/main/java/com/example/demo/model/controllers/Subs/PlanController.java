package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.Subs.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planes")
public class PlanController {
    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "Listar planes",
            description = "Devuelve una lista paginada de todos los planes de suscripción disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado de planes")
    })
    @GetMapping("/ver")
    public Page<PlanDTO> mostrarPlanes(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return planService.verTodos(pageable);
    }

    @Operation(summary = "Actualizar precio de un plan",
            description = "Actualiza el monto de un plan de suscripción existente por su tipo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado", content = @Content)
    })
    @PatchMapping("/{tipoPlan}/cambiarPrecio")
    public ResponseEntity<String> actualizarPlan(@PathVariable("tipoPlan") TipoSuscripcion tipoPlan,
                                                 @RequestParam float precio) {
        planService.cambiarMontoPlan(tipoPlan, precio);
        return ResponseEntity.ok("Plan actualizado correctamente");
    }

}
