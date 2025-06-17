package com.example.demo.model.DTOs.user;

import com.example.demo.model.enums.Genero;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para la creación de un nuevo usuario")
public class NewUsuarioDTO {

    @Schema(description = "Nombre del usuario", example = "Lautaro")
    @NotEmpty(message = "El nombre es necesario")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Martínez")
    @NotEmpty(message = "El apellido es necesario")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "lautaro@example.com")
    @Email
    @NotBlank(message = "El mail es requerido")
    private String email;

    @Schema(description = "Edad del usuario", example = "25")
    @Min(14)
    @Positive
    @NotNull(message = "Es necesario que ingrese su edad")
    private int edad;

    @Schema(description = "Teléfono del usuario", example = "1123456789")
    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    private String telefono;

    @Schema(description = "Nombre de usuario (no solo números, mínimo 4 caracteres)", example = "lautaM")
    @NotBlank(message = "El username es requerido")
    @Pattern(regexp = "^(?!\\d+$).+", message = "El username no puede ser solo números")
    @Length(min = 4, message = "El username debe tener un mínimo de 4 caracteres")
    private String username;

    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "segura123")
    @NotBlank(message = "La contraseña es requerida")
    @Length(min = 6, message = "La contraseña debe tener un mínimo de 6 caracteres")
    private String password;

    @Schema(description = "Géneros preferidos del usuario (mínimo 1, máximo 3)", example = "[\"ACCION\", \"COMEDIA\"]")
    @NotNull(message = "Debe elegir al menos dos géneros")
    @Size(min = 1, max = 3, message = "Debes elegir mínimo 1 género y máximo 3")
    private Set<Genero> generos;
}
