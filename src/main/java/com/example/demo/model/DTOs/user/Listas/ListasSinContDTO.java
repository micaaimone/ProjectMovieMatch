package com.example.demo.model.DTOs.user.Listas;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListasSinContDTO {
    @NotBlank(message = "la lista debe tener nombre")
    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    private String nombre;

    @NotNull(message = "Se debe elegir tipo de privacidad")
    private Boolean privado;
}
