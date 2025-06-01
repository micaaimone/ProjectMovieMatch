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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    })

    @GetMapping
    public ResponseEntity<Page<ContenidoDTO>> all(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(contenidoService.buscar(pageable, genero, anio, titulo, puntuacion, estado, clasificacion));
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

    @PostMapping("/{id}")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        if (contenidoService.darDeAltaBDD(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        if (contenidoService.borrarDeBDD(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}