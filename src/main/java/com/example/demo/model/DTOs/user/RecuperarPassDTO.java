package com.example.demo.model.DTOs.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarPassDTO {
    @NotBlank(message = "Se debe ingresar un mail para recuperar la contraseña")
    @Email
    private String mail;
}
