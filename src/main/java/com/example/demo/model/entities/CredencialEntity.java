package com.example.demo.model.entities;

import com.example.demo.model.enums.E_Cargo;
import jakarta.persistence.*;

//@Getter
//@Setter
//@RequiredArgsConstructor
//@ToString
//@NoArgsConstructor
@Entity
@Table(name = "credenciales")
public class CredencialEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rango", nullable = false, unique = true)
    private E_Cargo cargo;

    public CredencialEntity() {
    }

    public CredencialEntity(Long id, E_Cargo cargo) {
        this.id = id;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public E_Cargo getCargo() {
        return cargo;
    }

    public void setCargo(E_Cargo cargo) {
        this.cargo = cargo;
    }
}
