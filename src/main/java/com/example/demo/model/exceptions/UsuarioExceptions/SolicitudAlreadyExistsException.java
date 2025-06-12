package com.example.demo.model.exceptions.UsuarioExceptions;

public class SolicitudAlreadyExistsException extends RuntimeException {
  public SolicitudAlreadyExistsException(String message) {
    super(message);
  }
}
