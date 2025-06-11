package com.example.demo.model.exceptions.UsuarioExceptions;

public class LikeAlreadyExistsException extends RuntimeException {
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
