package com.example.demo.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {
    @NotBlank(message = "Debe ingresar un asunto")
    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    private String subject;

    @NotBlank(message = "Debe ingresar un mensaje")
    @Pattern(regexp = "^(?!\\d+$).+", message = "No puede ser solo números")
    private String mensaje;
}
