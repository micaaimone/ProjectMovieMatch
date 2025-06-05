package com.example.demo.model.entities;

import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "solicitud")
public class SolicitudAmistadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_solicitud;


    private long idEmisor;

    private long idReceptor;

    private EstadoSolicitud estadoSolicitud;

    //medio q lo pense como ig
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;

}
