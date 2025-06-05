package com.example.demo.model.DTOs;

import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewSolicitudAmistadDTO {

    @NotNull(message = "El id emisor de ser ingresado")
    private Long idEmisor;

    @NotNull(message = "El id receptor de ser ingresado")
    private Long idReceptor;

}
