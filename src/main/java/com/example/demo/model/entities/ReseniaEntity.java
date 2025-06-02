package com.example.demo.model.entities;


import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
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
public class ReseniaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_resenia;
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
