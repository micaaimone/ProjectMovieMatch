package com.example.demo.model.exceptions.SuscripcionException;

public class OfertaNotFoundException extends RuntimeException {
    public OfertaNotFoundException(String message) {
        super(message);
    }
}
