package com.example.demo.model.DTOs.Resenia;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContenidoLikeDTO {
    private Long id;

    private String poster;

    private Long idUsuario;

    private String username;

    private Long idContenido;

    private String titulo;

    private LocalDateTime fechaLike;
}
