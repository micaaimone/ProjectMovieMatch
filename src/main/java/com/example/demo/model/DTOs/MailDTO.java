package com.example.demo.model.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO utilizado para enviar correos electrónicos")
public class MailDTO {

    @NotBlank(message = "Debe ingresar un asunto")
    @Pattern(regexp = "^(?!\\d+$).+", message = "El asunto no puede ser solo números")
    @Schema(description = "Asunto del correo", example = "Problema con la cuenta")
    private String subject;

    @NotBlank(message = "Debe ingresar un mensaje")
    @Pattern(regexp = "^(?!\\d+$).+", message = "El mensaje no puede ser solo números")
    @Schema(description = "Mensaje del correo", example = "Hola, tengo un inconveniente con mi suscripción.")
    private String mensaje;
}
