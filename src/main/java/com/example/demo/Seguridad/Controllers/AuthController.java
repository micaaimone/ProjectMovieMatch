package com.example.demo.Seguridad.Controllers;

import com.example.demo.Seguridad.DTO.AuthRequest;
import com.example.demo.Seguridad.DTO.AuthResponse;
import com.example.demo.Seguridad.DTO.RefreshTokenRequest;
import com.example.demo.Seguridad.services.AuthService;
import com.example.demo.Seguridad.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user); // nuevo
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

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
}