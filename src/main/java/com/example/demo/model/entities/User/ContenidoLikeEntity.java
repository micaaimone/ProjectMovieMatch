package com.example.demo.model.entities.User;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import lombok.*;
import com.example.demo.model.enums.TipoReaccion;

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

    @Enumerated(EnumType.STRING)
    private TipoReaccion tipo; // LIKE o DISLIKE (esto en un futuro podemos poner algo como me encanta o cosas asi)
}
