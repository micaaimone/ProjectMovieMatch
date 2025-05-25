package com.example.demo.model.entities;

import com.example.demo.model.enums.E_Cargo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class CredencialEntity {
    @Id
    private long id;

    @Column(name = "rango", nullable = false)
    private E_Cargo cargo;
}
