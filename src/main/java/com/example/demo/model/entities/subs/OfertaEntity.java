package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "ofertas")
public class OfertaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_oferta;

    private String descripcion;
    private float descuento;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    @OneToOne
    @JoinColumn(name = "id_plan", nullable = true)
    private PlanSuscripcionEntity plan;
//-------------------------------------


    public OfertaEntity() {
    }

    public OfertaEntity(Long id_oferta, String descripcion, float descuento, LocalDate fecha_inicio, LocalDate fecha_fin, PlanSuscripcionEntity plan) {
        this.id_oferta = id_oferta;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.plan = plan;
    }

    public Long getId_oferta() {
        return id_oferta;
    }

    public void setId_oferta(Long id_oferta) {
        this.id_oferta = id_oferta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

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

    public PlanSuscripcionEntity getPlan() {
        return plan;
    }

    public void setPlan(PlanSuscripcionEntity plan) {
        this.plan = plan;
    }
}
