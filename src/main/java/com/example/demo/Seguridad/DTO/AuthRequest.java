package com.example.demo.Seguridad.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequest(

        @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
        String email,

        @Schema(description = "Contraseña del usuario", example = "12345678")
        String password

){
}
