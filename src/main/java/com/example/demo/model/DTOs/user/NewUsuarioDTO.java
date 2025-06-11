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
public class NewUsuarioDTO {
    @NotEmpty(message = "El nombre es necesario")
    private String nombre;

    @NotEmpty(message = "El apellido es necesario")
    private String apellido;

    @Email
    @NotBlank(message = "El mail es requerido")
    private String email;

    @Min(14)
    @Positive
    @NotNull(message = "Es necesario que ingrese su edad")
    private int edad;

    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El username es requerido")
    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    @Length(min = 4, message = "El username debe tener un minimo de 4 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    @Length(min = 6, message = "La contraseña debe tener un minimo de 6 caracteres")
    private String contrasenia;

    @NotNull(message = "Debe elegir al menos dos generos")
    @Size(min = 1, max = 3, message = "Debes elegir minimo 1 genero y maximo 3")
    private Set<Genero> generos;

}
