package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

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

}
