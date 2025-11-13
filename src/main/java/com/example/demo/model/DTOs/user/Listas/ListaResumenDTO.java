package com.example.demo.model.DTOs.user.Listas;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListaResumenDTO  {
    private Long id;
    private String nombre;
    private boolean privado;
}
