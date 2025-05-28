package com.example.demo.model.entities.subs;

import com.example.demo.model.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name ="suscripciones")
public class SuscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_suscripcion;

    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private UsuarioEntity usuario;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private boolean estado;
    private float monto;

    @ManyToOne
    private PlanSuscripcionEntity plan;

    @OneToMany(mappedBy = "suscripcion")
    private List<PagoEntity> pagos;
    //----------------------------------------------
}
