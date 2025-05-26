package com.example.demo.model.entities.subs;

import com.example.demo.model.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    public SuscripcionEntity(UsuarioEntity usuario, LocalDate fecha_inicio, LocalDate fecha_fin, boolean estado, float monto, PlanSuscripcionEntity plan) {
        this.usuario = usuario;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
        this.monto = monto;
        this.plan = plan;
        this.pagos = new ArrayList<>();
    }

    public SuscripcionEntity() {
    }

    public List<PagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoEntity> pagos) {
        this.pagos = pagos;
    }

    public Long getId_suscripcion() {
        return id_suscripcion;
    }

    public void setId_suscripcion(Long id_suscripcion) {
        this.id_suscripcion = id_suscripcion;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public PlanSuscripcionEntity getPlan() {
        return plan;
    }

    public void setPlan(PlanSuscripcionEntity plan) {
        this.plan = plan;
    }
}
