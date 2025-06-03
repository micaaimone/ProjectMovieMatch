package com.example.demo.model.exceptions;

public class ListaNotFoundException extends RuntimeException {
    public ListaNotFoundException(String message) {
        super(message);
    }
}
