package com.example.demo.model.entities;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "reseña")
public class ReseñaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_reseña;
//1 usuario a muchas reseñas

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

//1 pelicula muchas reseñas
    @ManyToOne
    @JoinColumn(name = "id_contenido")
    private ContenidoEntity contenido;


    private double puntuacionU;

    private String comentario;

    private LocalDateTime fecha;


}
