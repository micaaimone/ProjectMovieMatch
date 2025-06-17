package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Contenido.ContenidoService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private final UsuarioService usuarioService;

    @Autowired
    public ContenidoController(ContenidoService contenidoService, UsuarioService usuarioService) {
        this.contenidoService = contenidoService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Obtener todos los contenidos activos filtrados",
            description = "Devuelve una lista paginada de contenidos activos filtrados por género, año, título, puntuación, clasificación e ID. Todos los parámetros son opcionales."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de contenidos obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Parameters({
            @Parameter(name = "genero", description = "Género del contenido", schema = @Schema(type = "string", example = "Acción")),
            @Parameter(name = "anio", description = "Año de publicación", schema = @Schema(type = "string", example = "2023")),
            @Parameter(name = "titulo", description = "Título parcial o completo", schema = @Schema(type = "string", example = "Batman")),
            @Parameter(name = "puntuacion", description = "Puntuación mínima", schema = @Schema(type = "number", format = "double", example = "8.5")),
            @Parameter(name = "clasificacion", description = "Clasificación por edad (ej: PG-13)", schema = @Schema(type = "string", example = "PG-13")),
            @Parameter(name = "id", description = "ID del contenido", schema = @Schema(type = "integer", format = "int64", example = "10")),
            @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de elementos por página", schema = @Schema(type = "integer", defaultValue = "10"))
    })

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

    @Operation(
            summary = "Obtener contenidos dados de baja",
            description = "Devuelve una lista paginada de contenidos que han sido desactivados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de contenidos desactivados obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    //me lo deja ver aunque sea user normal
    @PreAuthorize("hasAuthority('VER_CONTENIDO_BAJA')")
    @GetMapping("/bajados")
    public ResponseEntity<Page<ContenidoDTO>> allDesactivados(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) String clasificacion,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contenidoService.buscarActivos(pageable, genero, anio, titulo, puntuacion, false, clasificacion, id));
    }

    @Operation(
            summary = "Dar de alta contenido",
            description = "Permite activar un contenido previamente desactivado mediante su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido dado de alta correctamente"),
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

    //me lo deja bajar aunque sea user normal
    @PreAuthorize("hasAuthority('DESACTIVAR_CONTENIDO')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<String> borrarContenido(@PathVariable long id) {
        contenidoService.darDeBajaContenido(id);
        return ResponseEntity.ok("Contenido eliminado correctamente.");
    }

    @PreAuthorize("hasAuthority('BUSCAR_NUEVO_CONTENIDO_POR_NOMBRE')")
    @GetMapping("/contenido/buscar-api")
    public ResponseEntity<ContenidoDTO> buscarContenidoDesdeAPI(@RequestParam String titulo) {
        return ResponseEntity.ok(contenidoService.buscarContenidoPorNombreDesdeAPI(titulo));
    }


    @PreAuthorize("hasAuthority('VISUALIZAR_RECOMENDACIONES_POR_LIKES')")
    @GetMapping("/recomendaciones")
    public ResponseEntity<Page<ContenidoDTO>> obtenerRecomendacionesPorLikes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Page<ContenidoDTO> recomendaciones = contenidoService.obtenerRecomendaciones(usuarioAutenticado.getId(), page, size);


        return ResponseEntity.ok(recomendaciones);
    }
}
