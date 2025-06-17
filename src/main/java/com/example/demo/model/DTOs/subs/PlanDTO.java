package com.example.demo.model.DTOs.subs;

import com.example.demo.model.entities.subs.TipoSuscripcion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class PlanDTO {
    @Schema(description = "Tipo de suscripción del plan", example = "MENSUAL")
    private TipoSuscripcion tipo;

    @Schema(description = "Precio base del plan sin descuento", example = "2499.99")
    private float precio;

    @Schema(description = "Lista de ofertas asociadas al plan")
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
