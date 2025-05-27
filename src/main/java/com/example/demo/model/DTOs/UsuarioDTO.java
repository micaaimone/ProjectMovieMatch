package com.example.demo.model.DTOs;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.CredencialEntity;


import java.util.Set;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class UsuarioDTO {
    private String username;
    private String email;
    private CredencialEntity credencial;
    //  private SuscripcionEntity suscripcion;
    private Set<ContenidoEntity> likes;

    public UsuarioDTO(String username, CredencialEntity credencial, Set<ContenidoEntity> likes) {
        this.username = username;
        this.credencial = credencial;
        this.likes = likes;
    }

    public UsuarioDTO() {
    }

    // GETTERS Y SETTERS

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CredencialEntity getCredencial() {
        return credencial;
    }

    public void setCredencial(CredencialEntity credencial) {
        this.credencial = credencial;
    }

    public Set<ContenidoEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<ContenidoEntity> likes) {
        this.likes = likes;
    }
}
