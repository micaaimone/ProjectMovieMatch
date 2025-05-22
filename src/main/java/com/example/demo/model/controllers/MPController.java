package com.example.demo.model.controllers;

import com.example.demo.model.services.MPService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/mp")
public class MPController {
    private final MPService mpService;

    public MPController(MPService mpService) {
        this.mpService = mpService;
    }

    @PostMapping("/pago")
    public ResponseEntity<Void> pago() {
        String init = mpService.crearPreferencia("mensual", 1500);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(init));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }



}
