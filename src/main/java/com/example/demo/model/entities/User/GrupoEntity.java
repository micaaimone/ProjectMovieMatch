package com.example.demo.model.entities.User;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupos")
@Getter
@Setter
@ToString
public class GrupoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;

    private String nombre;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_administrador", nullable = false)
    private UsuarioEntity administrador;

    @ManyToMany
    @JoinTable(
            name = "grupos_lista_usuarios",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<UsuarioEntity> listaUsuarios = new HashSet<>();

}