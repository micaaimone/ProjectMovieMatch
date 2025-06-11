package com.example.demo.model.exceptions;

public class SolicitudAlreadyExistsException extends RuntimeException {
  public SolicitudAlreadyExistsException(String message) {
    super(message);
  }
}
