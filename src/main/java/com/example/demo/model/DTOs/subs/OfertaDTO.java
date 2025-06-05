package com.example.demo.model.DTOs.subs;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OfertaDTO {
    @NotBlank(message = "La oferta debe tener una descripcion")
    private String descripcion;
    @NotNull(message = "La oferta debe tener descuento")
    @Positive
    @Min(value = 10, message = "el descuento no puede ser menor al 10%")
    @Max(value = 100, message = "El descuento no puede ser mayor al 100%")
    private Float descuento;

}
