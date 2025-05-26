package com.example.demo.model.DTOs.subs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private String medio_pago;
    private LocalDateTime fecha_pago;
    private BigDecimal monto_pago;

}
