package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "pagos")
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String medio_pago;
    private LocalDateTime fecha_pago;

    @ManyToOne
    @JoinColumn(name = "id_suscripcion", nullable = false)
    private SuscripcionEntity suscripcion;

}
