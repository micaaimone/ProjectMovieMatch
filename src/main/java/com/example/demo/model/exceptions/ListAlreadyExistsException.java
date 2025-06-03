package com.example.demo.model.exceptions;

public class ListAlreadyExistsException extends RuntimeException {
    public ListAlreadyExistsException(String message) {
        super(message);
    }
}
