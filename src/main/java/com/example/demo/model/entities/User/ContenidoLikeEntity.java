package com.example.demo.model.entities.User;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table
public class ContenidoLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuarioEntity usuario;

    @ManyToOne
    private ContenidoEntity contenido;

    @Column(nullable = false)
    private LocalDateTime fechaLike = LocalDateTime.now();

}
