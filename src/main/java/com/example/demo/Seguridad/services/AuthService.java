package com.example.demo.Seguridad.services;

import com.example.demo.Seguridad.DTO.AuthRequest;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    public AuthService(CredentialsRepository credentialsRepository,
                       AuthenticationManager authenticationManager) {
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;
    }
    public UserDetails authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );
        return
                credentialsRepository.findByEmail(input.username()).orElseThrow();
    }
}

