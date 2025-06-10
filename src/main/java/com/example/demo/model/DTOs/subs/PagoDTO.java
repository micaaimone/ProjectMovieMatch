package com.example.demo.model.DTOs.subs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class PagoDTO {
    @Schema(description = "ID único del pago", example = "101")
    private Long id;

    @Schema(description = "Medio de pago utilizado", example = "MercadoPago")
    private String medio_pago;

    @Schema(description = "Fecha y hora en que se realizó el pago", example = "2024-06-10T14:30:00")
    private LocalDateTime fecha_pago;

    @Schema(description = "Monto total del pago", example = "1500.00")
    private BigDecimal monto_pago;

    public PagoDTO(Long id, String medio_pago, LocalDateTime fecha_pago, BigDecimal monto_pago) {
        this.id = id;
        this.medio_pago = medio_pago;
        this.fecha_pago = fecha_pago;
        this.monto_pago = monto_pago;
    }

    public PagoDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedio_pago() {
        return medio_pago;
    }

    public void setMedio_pago(String medio_pago) {
        this.medio_pago = medio_pago;
    }

    public LocalDateTime getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDateTime fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public BigDecimal getMonto_pago() {
        return monto_pago;
    }

    public void setMonto_pago(BigDecimal monto_pago) {
        this.monto_pago = monto_pago;
    }
}
