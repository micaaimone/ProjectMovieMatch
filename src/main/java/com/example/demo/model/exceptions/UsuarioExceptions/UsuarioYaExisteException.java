package com.example.demo.model.exceptions.UsuarioExceptions;

public class UsuarioYaExisteException extends RuntimeException{
    public UsuarioYaExisteException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
