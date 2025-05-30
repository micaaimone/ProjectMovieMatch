package com.example.demo.model.entities.subs;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "listas_contenido")
public class ListasContenidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista_contenido")
    private Long idListaContenido;

    private String nombre;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToMany
    @JoinTable(
            name = "listasXcontenido",
            joinColumns = @JoinColumn(name = "id_lista_contenido"),
            inverseJoinColumns = @JoinColumn(name = "id_contenido"))
    private List<ContenidoEntity> contenidos;

}
