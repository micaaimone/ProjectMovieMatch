package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
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
}
