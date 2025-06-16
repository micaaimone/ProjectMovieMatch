package com.example.demo.model.DTOs.user.Listas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(min = 0, max = 1)
    @NotNull(message = "Se debe elegir tipo de privacidad")
    private Boolean privado;
}
