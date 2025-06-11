package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString

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

    public PlanSuscripcionEntity(TipoSuscripcion tipo, float precio, List<OfertaEntity> ofertas) {
        this.tipo = tipo;
        this.precio = precio;
        this.ofertas = ofertas;
    }
}
