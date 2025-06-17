package com.example.demo.model.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para recuperar la contraseña a través del correo electrónico")
public class RecuperarPassDTO {

    @Schema(description = "Correo electrónico asociado a la cuenta", example = "usuario@example.com")
    @NotBlank(message = "Se debe ingresar un mail para recuperar la contraseña")
    @Email
    private String mail;
}
