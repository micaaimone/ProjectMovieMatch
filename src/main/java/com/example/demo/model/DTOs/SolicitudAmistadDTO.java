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
public class SolicitudAmistadDTO {

    @NotNull(message = "El id de ser ingresado")
    private Long idReceptor;

    private EstadoSolicitud estadoSolicitud;
}
