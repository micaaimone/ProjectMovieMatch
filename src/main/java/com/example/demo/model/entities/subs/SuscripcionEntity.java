package com.example.demo.model.entities.subs;

import com.example.demo.model.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name ="suscripciones")
public class SuscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSuscripcion;

    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private UsuarioEntity usuario;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private boolean estado;
    private float monto;

    @ManyToOne
    private PlanSuscripcionEntity plan;

}
