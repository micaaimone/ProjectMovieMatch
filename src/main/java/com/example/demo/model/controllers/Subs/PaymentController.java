package com.example.demo.model.controllers.Subs;


import com.example.demo.model.services.Usuarios.UsuarioService;
import com.example.demo.model.services.payments.IPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final IPayment paymentService;

    @Autowired
    public PaymentController(IPayment paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> recibirWebhook(@RequestBody Map<String, Object> body) {
        paymentService.handleWebhook(body);
        return ResponseEntity.ok().build();
    }
}
