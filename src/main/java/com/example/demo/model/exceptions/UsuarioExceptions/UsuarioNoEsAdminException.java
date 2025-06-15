package com.example.demo.model.exceptions.UsuarioExceptions;

public class UsuarioNoEsAdminException extends RuntimeException {
    public UsuarioNoEsAdminException(String message) {
        super(message);
    }
}
