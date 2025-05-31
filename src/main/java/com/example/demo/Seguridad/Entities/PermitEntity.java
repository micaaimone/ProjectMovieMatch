package com.example.demo.Seguridad.Entities;

import com.example.demo.Seguridad.Enum.Permit;
import jakarta.persistence.*;

@Entity
public class PermitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    Permit permit;
}
