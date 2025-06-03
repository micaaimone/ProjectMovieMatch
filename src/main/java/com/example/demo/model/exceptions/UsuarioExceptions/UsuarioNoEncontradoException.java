package com.example.demo.model.exceptions.UsuarioExceptions;

public class UsuarioNoEncontradoException extends RuntimeException{
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
