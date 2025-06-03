package com.example.demo.model.exceptions.UsuarioExceptions;

public class ListaNotFoundException extends RuntimeException {
    public ListaNotFoundException(String message) {
        super(message);
    }
}
