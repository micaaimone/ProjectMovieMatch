package com.example.demo.model.DTOs.subs;


import java.time.LocalDate;
import java.util.List;


public class SuscripcionDTO {
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private boolean estado;
    private float monto;
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
