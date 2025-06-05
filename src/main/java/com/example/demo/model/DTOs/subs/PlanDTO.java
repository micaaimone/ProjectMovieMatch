package com.example.demo.model.DTOs.subs;

import com.example.demo.model.entities.subs.TipoSuscripcion;


import java.util.List;


public class PlanDTO {
    private TipoSuscripcion tipo;
    private float precio;
    private List<OfertaDTO> oferta;

    public PlanDTO(TipoSuscripcion tipo, float precio, List<OfertaDTO> oferta) {
        this.tipo = tipo;
        this.precio = precio;
        this.oferta = oferta;
    }

    public PlanDTO() {
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

    public List<OfertaDTO> getOferta() {
        return oferta;
    }

    public void setOferta(List<OfertaDTO> oferta) {
        this.oferta = oferta;
    }
}
