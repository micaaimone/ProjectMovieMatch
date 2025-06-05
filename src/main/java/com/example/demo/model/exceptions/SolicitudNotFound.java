package com.example.demo.model.exceptions;

public class SolicitudNotFound extends RuntimeException {
    public SolicitudNotFound(String message) {
        super(message);
    }
}
