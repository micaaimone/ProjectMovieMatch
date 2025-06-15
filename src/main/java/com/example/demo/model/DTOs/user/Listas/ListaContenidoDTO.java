package com.example.demo.model.DTOs.user.Listas;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListaContenidoDTO {
    private String nombre;
    private boolean privado;
    private List<ContenidoDTO> contenidos;
}
