package com.example.demo.model.controllers.Subs;

import com.example.demo.model.services.Subs.MPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mp")
public class MPController {
    private final MPService mpService;

    public MPController(MPService mpService) {
        this.mpService = mpService;
    }

    @Operation(
            summary = "Recibir notificación de Mercado Pago",
            description = """
                Este endpoint es invocado por Mercado Pago a través de su sistema de notificaciones (webhooks) 
                cuando ocurren eventos relevantes como pagos. Si el tipo de evento es 'payment', 
                se procesa la información correspondiente.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación recibida correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud malformada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la notificación", content = @Content)
    })
    @PostMapping("/notification")
    public ResponseEntity<String> recibirNoti(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo con los datos de la notificación enviada por Mercado Pago",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
            @RequestBody Map<String, Object> body
    ) {
        mpService.recibirPago(body);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Pago exitoso",
            description = "Endpoint al que Mercado Pago redirige al usuario cuando el pago fue aprobado exitosamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje de confirmación de pago exitoso")
    })
    @GetMapping("/success")
    public String pagoExitoso() {
        return "¡Pago exitoso!";
    }

    @Operation(
            summary = "Pago fallido",
            description = "Endpoint al que Mercado Pago redirige al usuario cuando el pago fue rechazado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje indicando que el pago fue rechazado")
    })
    @GetMapping("/failure")
    public String pagoFallido() {
        return "El pago fue rechazado";
    }

    @Operation(
            summary = "Pago pendiente",
            description = "Endpoint al que Mercado Pago redirige al usuario cuando el pago está pendiente de aprobación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje indicando que el pago está pendiente")
    })
    @GetMapping("/pending")
    public String pagoPendiente() {
        return "El pago está pendiente de aprobación";
    }

}

