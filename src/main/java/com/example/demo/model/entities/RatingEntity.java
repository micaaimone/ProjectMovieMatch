package com.example.demo.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ratings")
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_rating;

    @ManyToOne
    @JoinColumn(name = "id_contenido")
    private ContenidoEntity contenido;

    @JsonProperty("Source")
    private String fuente;

    @JsonProperty("Value")
    private String valor;
}
