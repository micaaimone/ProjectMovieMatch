package com.example.demo.model.DTOs.subs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionDTO {
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private boolean estado;
    private float monto;
    private List<PagoDTO> pagos;
}
