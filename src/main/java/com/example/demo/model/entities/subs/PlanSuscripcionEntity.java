package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "ofertas")

@Entity
@Table(name = "plan_suscripcion")
public class PlanSuscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoSuscripcion tipo;

    private BigDecimal precio;

    @OneToMany(mappedBy = "plan")
    private List <OfertaEntity> ofertas;

    //---------------------------------------

    public PlanSuscripcionEntity(TipoSuscripcion tipo, BigDecimal precio, List<OfertaEntity> ofertas) {
        this.tipo = tipo;
        this.precio = precio;
        this.ofertas = ofertas;
    }
}
