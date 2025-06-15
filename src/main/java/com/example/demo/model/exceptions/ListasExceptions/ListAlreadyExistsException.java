package com.example.demo.model.exceptions.ListasExceptions;

public class ListAlreadyExistsException extends RuntimeException {
    public ListAlreadyExistsException(String message) {
        super(message);
    }
}
