package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "pagos")
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String medio_pago;
    private LocalDateTime fecha_pago;
    private BigDecimal valor_pago;

    @ManyToOne
    @JoinColumn(name = "id_suscripcion", nullable = false)
    private SuscripcionEntity suscripcion;
}
