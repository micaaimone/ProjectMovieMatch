package com.example.demo.model.controllers.Subs;

import com.example.demo.model.DTOs.subs.SuscripcionDTO;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.services.Subs.MPService;
import com.example.demo.model.services.Subs.SuscripcionService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/suscripciones")
public class SuscripcionController {
    private final SuscripcionService suscripcionService;
    private final MPService mpService;

    public SuscripcionController(SuscripcionService suscripcionService, MPService mpService) {
        this.suscripcionService = suscripcionService;
        this.mpService = mpService;
    }

    /* ----esquema de como funciona
    Controller llama a MPService para generar link de pago → MpService llama a Subservice para crear una base de subEntity
                                ↓
    Nos dirigimos al link de pago y concretamos el pago, la api redirige a mpController/notification
                                ↓
    en notification recibimos el body del pago y verificamos q el status sea aprobado en recibir pago
                                ↓
    llamamos a gestion pago donde terminamos de armar la sub del paso 1 y enviamos el mail de confirmacion del pago
    */

    @Operation(
            summary = "Crear una suscripción",
            description = """
                Crea una nueva suscripción para el usuario especificado y devuelve la URL para iniciar el proceso de pago.

                **Flujo completo del proceso:**
                1. Controller llama a `MPService` para generar un link de pago.
                2. `MPService` llama a `SuscripcionService` para crear una base de subEntity.
                3. El usuario completa el pago y la API redirige a `/mp/notification`.
                4. En `notification`, se recibe el cuerpo del pago y se verifica que el estado sea 'aprobado'.
                5. Se llama a `gestionPago`, donde se finaliza la suscripción iniciada y se envía el mail de confirmación.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content)
    })
    @Parameters({
            @Parameter(name = "idUser", description = "ID del usuario que crea la suscripción", required = true, schema = @Schema(type = "integer", example = "5")),
            @Parameter(name = "tipo", description = "Tipo de suscripción", required = true, schema = @Schema(implementation = TipoSuscripcion.class))
    })
    @PreAuthorize("hasAuthority('SUSCRIPCION_CREAR')")
    @PostMapping("/{idUser}/crear")
    public ResponseEntity<String> crearSuscripcion(@PathVariable("idUser") Long idUsuario,
                                                   @RequestParam TipoSuscripcion tipo) throws MPException, MPApiException {
        String init = mpService.crearPreferencia(suscripcionService.save(idUsuario, tipo));
        return ResponseEntity.ok(init);
    }

    @Operation(
            summary = "Renovar una suscripción",
            description = "Renueva la suscripción activa del usuario y devuelve la URL para iniciar el proceso de pago."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción renovada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @Parameter(name = "idUsuario", description = "ID del usuario que renueva la suscripción", required = true, schema = @Schema(type = "integer", example = "5"))
    @PreAuthorize("hasAuthority('SUSCRIPCION_RENOVAR')")
    @PostMapping("/renovar")
    public ResponseEntity<String> renovarSuscripcion(@RequestParam Long idUsuario) throws MPException, MPApiException {
        String init = mpService.crearPreferencia(suscripcionService.renovar(idUsuario));
        return ResponseEntity.ok(init);
    }

    @Operation(
            summary = "Listar todas las suscripciones",
            description = "Devuelve una lista paginada de todas las suscripciones registradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('SUSCRIPCION_VER_TODAS')")
    @GetMapping("/mostrarTodos")
    public Page<SuscripcionDTO> mostrarSuscripciones(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return suscripcionService.findAll(pageable);
    }

    @Operation(
            summary = "Listar suscripciones activas",
            description = "Devuelve una lista paginada de suscripciones que se encuentran actualmente activas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de activas obtenido correctamente")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('SUSCRIPCION_VER_ACTIVAS')")
    @GetMapping("/mostrarActivos")
    public Page<SuscripcionDTO> mostrarActivos(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return suscripcionService.mostrarActivos(pageable);
    }

    @Operation(
            summary = "Obtener una suscripción por ID",
            description = "Devuelve los detalles de una suscripción específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada", content = @Content)
    })
    @Parameter(name = "id", description = "ID de la suscripción a buscar", required = true, schema = @Schema(type = "integer", example = "10"))
    @PreAuthorize("hasAuthority('SUSCRIPCION_VER_POR_ID')")
    @GetMapping("/mostrar/{id}")
    public ResponseEntity<SuscripcionDTO> mostrarSuscripcion(@PathVariable Long id) {
        return ResponseEntity.ok(suscripcionService.findById(id));
    }


}
