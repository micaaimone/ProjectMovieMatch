package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.ListasSinContDTO;
import com.example.demo.model.services.Usuarios.ListasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/listas")
public class ListasController {
    private final ListasService listasService;

    @Autowired
    public ListasController(ListasService listasService) {
        this.listasService = listasService;
    }

    // metodos de listas de contenidos--------------------------

    @Operation(summary = "Crear una nueva lista",
            description = "Crea una lista vacía para un usuario específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("hasAuthority('LISTA_CREAR')")
    @PostMapping("/{id}/crearLista")
    public ResponseEntity<String> crearLista(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long idUser,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO con el nombre y estado de privacidad de la lista",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ListasSinContDTO.class)))
            @Valid @RequestBody ListasSinContDTO lista
    ) {
        listasService.addLista(idUser, lista);
        return ResponseEntity.ok("Lista Creada!");
    }

    @Operation(summary = "Agregar contenido a una lista",
            description = "Agrega un contenido a la lista de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido agregado a la lista correctamente")
    })
    @PreAuthorize("hasAuthority('LISTA_AGREGAR_CONTENIDO')")
    @PatchMapping("/{id}/agregarALista")
    public ResponseEntity<String> agregarALista(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nombre de la lista a modificar", required = true)
            @RequestParam String nombre
    ) {
        listasService.agregarContenido(id, nombre);
        return ResponseEntity.ok("Lista Agregada!");
    }

    // mostrar listas con contenido y sin----------------------------------

    @Operation(summary = "Ver todas las listas de un usuario",
            description = "Devuelve una lista paginada de las listas (sin contenido) que pertenecen al usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listas obtenidas correctamente")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(defaultValue = "5"))
    })
    @PreAuthorize("hasAuthority('LISTA_VER_PROPIAS')")
    @GetMapping("/{id}/verListas")
    public ResponseEntity<Page<ListasSinContDTO>> verListas(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long idUser,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(listasService.getListas(idUser, pageable));
    }

    // ------------------ Ver una lista específica

    @Operation(summary = "Ver una lista específica",
            description = "Devuelve una lista con su contenido por nombre para un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista encontrada exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_VER_DETALLE')")
    @GetMapping("/{id}/verLista/{nombre}")
    public ResponseEntity<ListaContenidoDTO> verLista(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long idUser,

            @Parameter(description = "Nombre de la lista", required = true)
            @PathVariable("nombre") String nombre
    ) {
        return ResponseEntity.ok(listasService.verListaXnombre(idUser, nombre));
    }

    //-------------------- editar variables de listas ------------

    @Operation(summary = "Cambiar el nombre de una lista",
            description = "Cambia el nombre de una lista existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre cambiado exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_NOMBRE')")
    @PatchMapping("/{id}/{nombre}/cambiarNombre")
    public ResponseEntity<String> cambiarNombre(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nombre actual de la lista", required = true)
            @PathVariable("nombre") String nombre,

            @Parameter(description = "Nuevo nombre de la lista", required = true)
            @RequestParam String newNombre
    ) {
        listasService.cambiarNombre(id, nombre, newNombre);
        return ResponseEntity.ok("Nombre cambiado!");
    }

    // ------------------ Cambiar estado de privacidad

    @Operation(summary = "Cambiar privacidad de una lista",
            description = "Modifica el estado de privacidad de una lista (pública o privada).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Privacidad cambiada correctamente")
    })
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_ESTADO')")
    @PatchMapping("/{id}/{nombre}/cambiarEstado")
    public ResponseEntity<String> cambiarEstado(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nombre de la lista", required = true)
            @PathVariable("nombre") String nombre,

            @Parameter(description = "Nuevo estado de privacidad", required = true)
            @RequestParam boolean newEstado
    ) {
        listasService.cambiarPrivado(id, nombre, newEstado);
        return ResponseEntity.ok("Privacidad cambiada!");
    }

    // ------------------------ borrar contenido o borrar lista completa------------

    @Operation(summary = "Eliminar contenido de una lista",
            description = "Quita un contenido de la lista especificada por nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido eliminado de la lista")
    })
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR_CONTENIDO')")
    @DeleteMapping("/{id}/sacarDelista")
    public ResponseEntity<String> eliminarDeLista(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nombre de la lista", required = true)
            @RequestParam String nombre
    ) {
        listasService.eliminarContenido(id, nombre);
        return ResponseEntity.ok("Contenido eliminado!");
    }

    // ------------------ Eliminar lista completa

    @Operation(summary = "Eliminar una lista",
            description = "Elimina por completo una lista de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista eliminada exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR')")
    @DeleteMapping("/{id}/eliminarLista")
    public ResponseEntity<String> eliminarLista(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("id") Long idUser,

            @Parameter(description = "Nombre de la lista a eliminar", required = true)
            @RequestParam String nombre
    ) {
        listasService.eliminarLista(idUser, nombre);
        return ResponseEntity.ok("Lista Eliminada!");
    }

    // ------------------ Ver listas de otro usuario

    @Operation(summary = "Ver listas públicas de otro usuario",
            description = "Devuelve las listas públicas de un usuario específico por su nombre de usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listas obtenidas correctamente")
    })
    @Parameter(name = "username", description = "Nombre de usuario", required = true)
    @PreAuthorize("hasAuthority('LISTA_VER_PUBLICAS')")
    @GetMapping("/{username}/verListasDeUser")
    public ResponseEntity<Page<ListaContenidoDTO>> verListas(
            @PathVariable("username") String username,
            Pageable pageable
    ) {
        return ResponseEntity.ok(listasService.verListaDeUser(username, pageable));
    }


}
