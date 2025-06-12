package com.example.demo.model.DTOs.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewSolicitudAmistadDTO {

    @NotNull(message = "El id emisor de ser ingresado")
    @Positive
    private Long idEmisor;

    @NotNull(message = "El id receptor de ser ingresado")
    @Positive
    private Long idReceptor;

}