package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.services.Contenido.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/contenido")
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @Operation(
            summary = "Obtener todos los contenidos filtrados",
            description = "Devuelve una lista paginada de contenidos filtrados por género, año, título, puntuación, estado y clasificación. Los parámetros son opcionales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contenidos obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    }
    )

    @PreAuthorize("hasAuthority('VER_CONTENIDO_ACTIVO')")
    @GetMapping
    public ResponseEntity<Page<ContenidoDTO>> allActivos(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contenidoService.buscarActivos(pageable, genero, anio, titulo, puntuacion, true, clasificacion, id));
    }

    @PreAuthorize("hasAuthority('VER_CONTENIDO_BAJA')")
    @GetMapping("/bajados")
    public ResponseEntity<Page<ContenidoDTO>> allDesactivados(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contenidoService.buscarActivos(pageable, genero, anio, titulo, puntuacion, false, clasificacion, id));
    }

    @Operation(
            summary = "Dar de alta contenido",
            description = "Permite dar de alta un contenido previamente dado de baja mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenido dado de alta correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el contenido con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "ID inválido proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PreAuthorize("hasAuthority('ACTIVAR_CONTENIDO')")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<String> darDeAlta(@PathVariable Long id) {
        contenidoService.darDeAltaContenido(id);
        return ResponseEntity.ok("Contenido dado de alta correctamente.");
    }

    @Operation(
            summary = "Eliminar contenido",
            description = "Elimina un contenido de la base de datos por su ID. Puede ser una eliminación lógica o física según la implementación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el contenido con el ID proporcionado"),
            @ApiResponse(responseCode = "400", description = "ID inválido proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PreAuthorize("hasAuthority('DESACTIVAR_CONTENIDO')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<String> borrarContenido(@PathVariable long id) {
        contenidoService.darDeBajaContenido(id);
        return ResponseEntity.ok("Contenido eliminado correctamente.");
    }
}
