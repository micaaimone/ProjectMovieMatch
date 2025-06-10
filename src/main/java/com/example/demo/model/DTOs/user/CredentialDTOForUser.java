package com.example.demo.model.DTOs.user;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CredentialDTOForUser {
    private String email;
    private Set<String> roles;

}
