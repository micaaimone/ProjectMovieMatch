package com.example.demo.model.exceptions.ListasExceptions;

public class ListaNotFoundException extends RuntimeException {
    public ListaNotFoundException(String message) {
        super(message);
    }
}
