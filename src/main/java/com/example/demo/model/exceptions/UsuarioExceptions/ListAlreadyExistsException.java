package com.example.demo.model.exceptions.UsuarioExceptions;

public class ListAlreadyExistsException extends RuntimeException {
    public ListAlreadyExistsException(String message) {
        super(message);
    }
}
