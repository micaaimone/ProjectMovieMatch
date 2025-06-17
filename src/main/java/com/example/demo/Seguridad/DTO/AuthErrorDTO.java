package com.example.demo.Seguridad.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AuthErrorDTO(

        @Schema(description = "Fecha y hora del error", example = "2025-06-10T15:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Código de estado HTTP", example = "401")
        int status,

        @Schema(description = "Tipo de error", example = "Unauthorized")
        String error,

        @Schema(description = "Mensaje de error detallado", example = "Token inválido o expirado")
        String message,

        @Schema(description = "Ruta donde ocurrió el error", example = "/auth")
        String path
){

}
