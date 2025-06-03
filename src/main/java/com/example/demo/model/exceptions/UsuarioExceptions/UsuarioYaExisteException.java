package com.example.demo.model.exceptions.UsuarioExceptions;

public class UsuarioYaExisteException extends RuntimeException{
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}
