package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

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
}
