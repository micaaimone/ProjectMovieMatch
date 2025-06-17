package com.example.demo.Seguridad.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(

        @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Refresh token para renovar el token de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken

){

}
