package com.example.demo.model.entities.subs;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

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

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private PlanSuscripcionEntity plan;

}
