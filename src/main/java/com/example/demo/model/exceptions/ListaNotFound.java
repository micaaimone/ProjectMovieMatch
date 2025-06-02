package com.example.demo.model.exceptions;

public class ListaNotFound extends RuntimeException {
    public ListaNotFound(String message) {
        super(message);
    }
}
