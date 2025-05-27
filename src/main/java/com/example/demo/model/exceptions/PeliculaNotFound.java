package com.example.demo.model.exceptions;

public class PeliculaNotFound extends RuntimeException {
    public PeliculaNotFound(String message) {
        super(message);
    }
}
