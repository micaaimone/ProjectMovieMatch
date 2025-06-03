package com.example.demo.model.DTOs.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UsuarioModificarDTO {
    @Email
    private String email;

    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    @Length(min = 4, message = "El username debe tener un minimo de 4 caracteres")
    private String username;

    @Length(min = 6, message = "La contraseña debe tener un minimo de 6 caracteres")
    private String contrasenia;
}
