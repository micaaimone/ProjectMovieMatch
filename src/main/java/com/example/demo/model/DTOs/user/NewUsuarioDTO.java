package com.example.demo.model.DTOs.user;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewUsuarioDTO {
    @NotEmpty(message = "El nombre es necesario")
    private String nombre;

    @NotEmpty(message = "El apellido es necesario")
    private String apellido;

    @Email
    private String email;

    @Min(14)
    @Positive
    @NotNull(message = "Es necesario que ingrese su edad")
    private int edad;

    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El username es requerido")
    @Length(min = 4, message = "El username debe tener un minimo de 4 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    @Length(min = 6, message = "La contraseña debe tener un minimo de 6 caracteres")
    private String contrasenia;



}
