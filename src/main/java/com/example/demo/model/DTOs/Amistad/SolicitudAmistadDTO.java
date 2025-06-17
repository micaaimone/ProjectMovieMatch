package com.example.demo.model.DTOs.Amistad;

import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SolicitudAmistadDTO {

    @NotNull(message = "El id de ser ingresado")
    @Positive
    private Long idReceptor;

    private Long idEmisor;

    private EstadoSolicitud estadoSolicitud;
}
