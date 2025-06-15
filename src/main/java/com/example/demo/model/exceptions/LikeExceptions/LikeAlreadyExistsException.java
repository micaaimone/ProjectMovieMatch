package com.example.demo.model.exceptions.LikeExceptions;

public class LikeAlreadyExistsException extends RuntimeException {
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
