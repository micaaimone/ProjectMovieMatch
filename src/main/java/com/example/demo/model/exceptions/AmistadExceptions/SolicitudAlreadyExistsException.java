package com.example.demo.model.exceptions.AmistadExceptions;

public class SolicitudAlreadyExistsException extends RuntimeException {
  public SolicitudAlreadyExistsException(String message) {
    super(message);
  }
}
