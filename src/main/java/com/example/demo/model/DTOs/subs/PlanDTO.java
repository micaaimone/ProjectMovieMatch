package com.example.demo.model.DTOs.subs;

import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private TipoSuscripcion tipo;
    private float precio;
    private OfertaEntity oferta;
}
