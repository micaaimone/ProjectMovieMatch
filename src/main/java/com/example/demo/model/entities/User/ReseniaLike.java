package com.example.demo.model.entities.User;

import com.example.demo.model.entities.Contenido.ReseniaEntity;
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
public class ReseniaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuarioEntity usuario;

    @ManyToOne
    private ReseniaEntity resenia;

    @Column(nullable = false)
    private LocalDateTime fechaLike = LocalDateTime.now();

}
