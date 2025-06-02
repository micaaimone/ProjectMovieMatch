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
        try {
            if ("payment".equals(body.get("type"))) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                if (data != null && data.get("id") != null) {
                    Long paymentId = Long.valueOf(data.get("id").toString());
                    mpService.procesarPago(paymentId);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

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

