package com.example.demo.model.entities.User;

import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "solicitudes")
public class AmistadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_solicitud;


    private long idEmisor;

    private long idReceptor;

    private EstadoSolicitud estadoSolicitud;


}
