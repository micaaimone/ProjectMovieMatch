package com.example.demo.model.DTOs.user;

import com.example.demo.model.enums.Genero;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

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

    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    @Length(min = 4, message = "El username debe tener un minimo de 4 caracteres")
    private String username;

    @Length(min = 6, message = "La contraseña debe tener un minimo de 6 caracteres")
    private String contrasenia;

    @Size(min = 1, max = 3, message = "Debes elegir minimo 1 genero y maximo 3")
    private Set<Genero> generos;
}
