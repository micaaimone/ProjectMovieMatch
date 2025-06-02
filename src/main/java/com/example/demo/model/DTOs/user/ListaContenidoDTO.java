package com.example.demo.model.DTOs.user;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
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
