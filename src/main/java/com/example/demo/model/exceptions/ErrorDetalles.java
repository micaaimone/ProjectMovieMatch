package com.example.demo.model.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor


//nos sirve para darle un "cuerpo" y que se vea mas lindo y entendible el error
public class ErrorDetalles {
    private String mensaje;
    private int status;
    private LocalDateTime timestamp;

}
