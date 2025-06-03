package com.example.demo.model.exceptions.SuscripcionException;

public class SubAlreadyExistException extends RuntimeException {
    public SubAlreadyExistException(String message) {
        super(message);
    }
}
