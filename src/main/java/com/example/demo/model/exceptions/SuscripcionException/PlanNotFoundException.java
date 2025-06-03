package com.example.demo.model.exceptions.SuscripcionException;

public class PlanNotFoundException extends RuntimeException {
    public PlanNotFoundException(String message) {
        super(message);
    }
}
