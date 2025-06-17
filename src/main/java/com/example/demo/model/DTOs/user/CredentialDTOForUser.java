package com.example.demo.model.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "DTO con información de credenciales y roles del usuario")
public class CredentialDTOForUser {

    @Schema(description = "Email del usuario", example = "usuario@example.com")
    private String email;

    @Schema(description = "Roles asociados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;
}
