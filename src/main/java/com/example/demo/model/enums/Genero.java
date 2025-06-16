package com.example.demo.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Genero {
    Action, Adventure, Animation, Biography, Comedy,
    Crime, Documentary, Drama, Family, Fantasy, History,
    Horror, Music, Musical, Mystery, Romance ,SciFi,
    Sport,Thriller,War, Western;

    @JsonCreator
    public static Genero fromValue(String value) {
//        for (Genero genero : Genero.values()) {
//            if (genero.name().equalsIgnoreCase(value)) {
//                return genero;
//            }
//        }

        // para evitar los espacios
        if (value == null) return null;

        String cleanValue = value.trim().replace(" ", "").toLowerCase();

        for (Genero genero : Genero.values()) {
            if (genero.name().replace(" ", "").toLowerCase().equals(cleanValue)) {
                return genero;
            }
        }

        String opcionesDisponibles = Arrays.stream(Genero.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        throw new IllegalArgumentException("Género inválido: '" + value
                + "'. Los valores válidos son: " + opcionesDisponibles);
    }
}
