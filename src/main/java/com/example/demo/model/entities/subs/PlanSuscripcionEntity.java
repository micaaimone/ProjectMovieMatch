package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "plan_suscripcion")
public class PlanSuscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoSuscripcion tipo;

    private float precio;

    @OneToMany(mappedBy = "plan")
    private List <OfertaEntity> ofertas;

    //---------------------------------------


    public PlanSuscripcionEntity( TipoSuscripcion tipo, float precio) {
        this.tipo = tipo;
        this.precio = precio;
        this.ofertas = new ArrayList<>();
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

    public List<OfertaEntity> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<OfertaEntity> ofertas) {
        this.ofertas = ofertas;
    }
}
