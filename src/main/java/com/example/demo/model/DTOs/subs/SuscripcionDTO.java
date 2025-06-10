package com.example.demo.model.DTOs.subs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


public class SuscripcionDTO {
    @Schema(description = "Fecha en que comienza la suscripción", example = "2025-06-10")
    private LocalDate fecha_inicio;

    @Schema(description = "Fecha en que finaliza la suscripción", example = "2025-07-10")
    private LocalDate fecha_fin;

    @Schema(description = "Estado actual de la suscripción (activa/inactiva)", example = "true")
    private boolean estado;

    @Schema(description = "Monto total pagado por la suscripción", example = "2499.99")
    private float monto;

    @Schema(description = "Lista de pagos asociados a la suscripción")
    private List<PagoDTO> pagos;

    public SuscripcionDTO(LocalDate fecha_inicio, LocalDate fecha_fin, boolean estado, float monto, List<PagoDTO> pagos) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
        this.monto = monto;
        this.pagos = pagos;
    }

    public SuscripcionDTO() {}

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public List<PagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoDTO> pagos) {
        this.pagos = pagos;
    }
}
