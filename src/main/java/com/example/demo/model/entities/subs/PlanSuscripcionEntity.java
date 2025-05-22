package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "plan_suscripcion")
public class PlanSuscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoSuscripcion tipo;

    private float precio;

    @OneToOne
    @JoinColumn(name = "id_oferta", nullable = true)
    private OfertaEntity oferta;

    //---------------------------------------


    public PlanSuscripcionEntity(Long id, TipoSuscripcion tipo, float precio, OfertaEntity oferta) {
        this.id = id;
        this.tipo = tipo;
        this.precio = precio;
        this.oferta = oferta;
    }

    public PlanSuscripcionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoSuscripcion getTipo() {
        return tipo;
    }

    public void setTipo(TipoSuscripcion tipo) {
        this.tipo = tipo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public OfertaEntity getOferta() {
        return oferta;
    }

    public void setOferta(OfertaEntity oferta) {
        this.oferta = oferta;
    }
}
