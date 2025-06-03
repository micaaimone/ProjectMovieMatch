package com.example.demo.model.exceptions.SuscripcionException;

public class PagoNotFoundException extends RuntimeException {
    public PagoNotFoundException(String message) {
        super(message);
    }
}
