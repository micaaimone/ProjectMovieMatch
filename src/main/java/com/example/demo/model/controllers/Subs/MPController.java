package com.example.demo.model.controllers.Subs;

import com.example.demo.model.services.Subs.MPService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mp")
public class MPController {
    private final MPService mpService;

    public MPController(MPService mpService) {
        this.mpService = mpService;
    }

    @PostMapping("/notification")
    public ResponseEntity <Void> recibirNoti(@RequestBody Map<String, Object> body){
        mpService.recibirPago(body);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/success")
    public String pagoExitoso() {
        return "¡Pago exitoso!";
    }

    @GetMapping("/failure")
    public String pagoFallido() {
        return "El pago fue rechazado";
    }

    @GetMapping("/pending")
    public String pagoPendiente() {
        return "El pago está pendiente de aprobación";
    }

}

