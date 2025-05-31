package com.example.demo.model.entities;

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

        @NotBlank
        private String nombre;

        @ManyToOne
        @JoinColumn(name = "id_administrador", nullable = false)
        private UsuarioEntity administrador;

        @ManyToMany
        private Set<UsuarioEntity> listaUsuarios = new HashSet<>();

        @ManyToMany
        private Set<ContenidoEntity> listasPeliculas = new HashSet<>();

}
