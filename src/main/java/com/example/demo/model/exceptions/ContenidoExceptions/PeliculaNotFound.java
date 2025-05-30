package com.example.demo.model.exceptions.ContenidoExceptions;

public class PeliculaNotFound extends RuntimeException {
    public PeliculaNotFound(String message) {
        super(message);
    }
}
