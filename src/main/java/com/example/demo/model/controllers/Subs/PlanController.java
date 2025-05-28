package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.services.Subs.PlanService;
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

    @GetMapping("/ver")
    public Page<PlanDTO> mostrarPlanes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        return planService.verTodos(pageable);

    }

    @PatchMapping("/cambiarPrecio/{id}")
    public ResponseEntity<Void> actualizarPlan(@PathVariable float monto, @PathVariable long id) {
        planService.cambiarMontoPlan(monto,id);
        return ResponseEntity.ok().build();
    }

    //falta el de actualizar monto de plan
}
