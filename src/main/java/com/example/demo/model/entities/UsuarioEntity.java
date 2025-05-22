package com.example.demo.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

}
