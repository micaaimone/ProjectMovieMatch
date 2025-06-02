package com.example.demo.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "series")
@PrimaryKeyJoinColumn(name = "serie_id")
public class SerieEntity extends ContenidoEntity{

    @JsonProperty("totalSeasons")
    private String totalSeasons;
}
