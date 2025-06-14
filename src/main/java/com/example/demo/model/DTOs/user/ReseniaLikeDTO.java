package com.example.demo.model.DTOs.user;

import com.example.demo.model.entities.Contenido.ReseniaEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReseniaLikeDTO {
    private Long id;

    private Long idUsuario;
    private String username;

    private Long idResenia;
    private String titulo;
    private Double puntuacion;

    private LocalDateTime fechaLike = LocalDateTime.now();
}
