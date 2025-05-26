package com.example.demo.model.entities.subs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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

    public PagoEntity(String medio_pago, LocalDateTime fecha_pago, BigDecimal valor_pago, SuscripcionEntity suscripcion) {
        this.medio_pago = medio_pago;
        this.fecha_pago = fecha_pago;
        this.valor_pago = valor_pago;
        this.suscripcion = suscripcion;
    }
    public PagoEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedio_pago() {
        return medio_pago;
    }

    public void setMedio_pago(String medio_pago) {
        this.medio_pago = medio_pago;
    }

    public LocalDateTime getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDateTime fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public BigDecimal getValor_pago() {
        return valor_pago;
    }

    public void setValor_pago(BigDecimal valor_pago) {
        this.valor_pago = valor_pago;
    }

    public SuscripcionEntity getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(SuscripcionEntity suscripcion) {
        this.suscripcion = suscripcion;
    }
}
