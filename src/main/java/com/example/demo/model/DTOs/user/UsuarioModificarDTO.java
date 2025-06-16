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
@Schema(description = "DTO para modificar datos del usuario")
public class UsuarioModificarDTO {

    @Email
    @Schema(description = "Email del usuario", example = "usuario@email.com")
    private String email;

    @Size(min = 10, max = 15, message = "El teléfono debe tener entre 10 y 15 caracteres")
    @Schema(description = "Número de teléfono", example = "1134567890")
    private String telefono;

    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    @Length(min = 4, message = "El username debe tener un mínimo de 4 caracteres")
    @Schema(description = "Nombre de usuario", example = "juan123")
    private String username;

    @Length(min = 6, message = "La contraseña debe tener un mínimo de 6 caracteres")
    @Schema(description = "Contraseña nueva", example = "miContraseña123")
    private String password;

    @Size(min = 1, max = 3, message = "Debes elegir mínimo 1 género y máximo 3")
    @Schema(description = "Conjunto de géneros preferidos")
    private Set<Genero> generos;
}
