package com.example.demo.model.exceptions;

public class UsuarioNoEncontradoException extends RuntimeException{
    public UsuarioNoEncontradoException(Long id) {
        super("No se encontró el usuario con ID: " + id);
    }
}
