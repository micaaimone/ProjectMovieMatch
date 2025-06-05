package com.example.demo.model.DTOs.subs;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public class PagoDTO {
    private Long id;
    private String medio_pago;
    private LocalDateTime fecha_pago;
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
