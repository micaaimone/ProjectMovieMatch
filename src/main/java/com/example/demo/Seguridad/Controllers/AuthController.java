package com.example.demo.Seguridad.Controllers;

import com.example.demo.Seguridad.DTO.AuthRequest;
import com.example.demo.Seguridad.DTO.AuthResponse;
import com.example.demo.Seguridad.DTO.RefreshTokenRequest;
import com.example.demo.Seguridad.services.AuthService;
import com.example.demo.Seguridad.services.JwtService;
import com.example.demo.Seguridad.services.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    public AuthController(AuthService authService, JwtService jwtService, TokenBlacklistService blacklistService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }
    @Operation(
            summary = "Autenticación de usuario",
            description = "Recibe las credenciales del usuario y devuelve un token de acceso junto con un refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user); // nuevo
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }
    @Operation(
            summary = "Renovar token de acceso",
            description = "Usa un refresh token válido para obtener un nuevo token de acceso y un nuevo refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado correctamente"),
            @ApiResponse(responseCode = "400", description = "Refresh token inválido o expirado", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);
        UserDetails user = authService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, user)) {
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user); // opcional: refresh rotativo
            return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
        }

        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody RefreshTokenRequest refreshTokenRequest) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            blacklistService.blacklist(token); // invalida el access
        }

        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshToken != null && !refreshToken.isBlank()) {
            blacklistService.blacklist(refreshToken); // invalida el refresh
        }

        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }

}