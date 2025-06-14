package com.example.demo.model.entities.Contenido;


import com.example.demo.model.entities.User.ReseniaLikeEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "resenia", cascade = CascadeType.ALL)
    private List<ReseniaLikeEntity> likes = new ArrayList<>();


}
