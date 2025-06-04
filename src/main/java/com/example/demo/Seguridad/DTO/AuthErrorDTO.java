package com.example.demo.Seguridad.DTO;

import java.time.LocalDateTime;

public record AuthErrorDTO(LocalDateTime timestamp,
                           int status,
                           String error,
                           String message,
                           String path) {

}
