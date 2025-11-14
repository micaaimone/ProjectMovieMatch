package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.ResponseDTO;
import com.example.demo.model.DTOs.user.Listas.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.Listas.ListasSinContDTO;
import com.example.demo.model.DTOs.user.Listas.ListaResumenDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Usuarios.ListasService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios/listas")
public class ListasController {
    private final ListasService listasService;
    //uso el service unicamente por la autenticacion
    private final UsuarioService usuarioService;

    @Autowired
    public ListasController(ListasService listasService, UsuarioService usuarioService) {
        this.listasService = listasService;
        this.usuarioService = usuarioService;
    }

    // metodos de listas de contenidos--------------------------

    @Operation(summary = "Crear una nueva lista",
            description = "Crea una lista vacía para un usuario específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("hasAuthority('LISTA_CREAR')")
    @PostMapping("/crearLista")
    public ResponseEntity<ResponseDTO> crearLista(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO con el nombre y estado de privacidad de la lista",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ListasSinContDTO.class)))
            @Valid @RequestBody ListasSinContDTO lista
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.addLista(usuarioAutenticado.getId(), lista);
        return ResponseEntity.ok(new ResponseDTO("Lista Creada!"));
    }

    @Operation(summary = "Agregar contenido a una lista",
            description = "Agrega un contenido a la lista de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido agregado a la lista correctamente")
    })
    @PreAuthorize("hasAuthority('LISTA_AGREGAR_CONTENIDO')")
    @PatchMapping("/agregarALista/{idLista}")
    public ResponseEntity<ResponseDTO> agregarALista(
            @Parameter(description = "Nombre del contenido a agregar", required = true)
            @RequestParam String nombre,
            @PathVariable Long idLista
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.agregarContenido(idLista, nombre);
        return ResponseEntity.ok(new ResponseDTO("Contenido agregado!"));
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
    @GetMapping("/verListas")
    public ResponseEntity<Page<ListaResumenDTO>> verListas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(listasService.getListas(usuarioAutenticado.getId(), pageable));
    }

    // ------------------ Ver una lista específica

    @Operation(summary = "Ver una lista específica",
            description = "Devuelve una lista con su contenido por nombre para un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista encontrada exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_VER_DETALLE')")
    @GetMapping("/verLista/{nombre}")
    public ResponseEntity<ListaContenidoDTO> verLista(
            @Parameter(description = "Nombre de la lista", required = true)
            @PathVariable("nombre") String nombre
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(listasService.verListaXnombre(usuarioAutenticado.getId(), nombre));
    }

    //-------------------- editar variables de listas ------------

    @Operation(summary = "Cambiar el nombre de una lista",
            description = "Cambia el nombre de una lista existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nombre cambiado exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_NOMBRE')")
    @PatchMapping("/{nombre}/cambiarNombre")
    public ResponseEntity<ResponseDTO> cambiarNombre(

            @Parameter(description = "Nombre actual de la lista", required = true)
            @PathVariable("nombre") String nombre,

            @Parameter(description = "Nuevo nombre de la lista", required = true)
            @RequestParam String newNombre
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.cambiarNombre(usuarioAutenticado.getId(), nombre, newNombre);
        return ResponseEntity.ok(new ResponseDTO("Nombre cambiado!"));
    }

    // ------------------ Cambiar estado de privacidad

    @Operation(summary = "Cambiar privacidad de una lista",
            description = "Modifica el estado de privacidad de una lista (pública o privada).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Privacidad cambiada correctamente")
    })
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_ESTADO')")
    @PatchMapping("/{nombre}/cambiarEstado")
    public ResponseEntity<ResponseDTO> cambiarEstado(

            @Parameter(description = "Nombre de la lista", required = true)
            @PathVariable("nombre") String nombre,

            @Parameter(description = "Nuevo estado de privacidad", required = true)
            @RequestParam boolean newEstado
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.cambiarPrivado(usuarioAutenticado.getId(), nombre, newEstado);
        return ResponseEntity.ok(new ResponseDTO("Privacidad cambiada!"));
    }

    // ------------------------ borrar contenido o borrar lista completa------------

    @Operation(
            summary = "Eliminar contenido de una lista",
            description = "Quita un contenido de la lista especificada por nombre."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contenido eliminado de la lista")
    })
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR_CONTENIDO')")
    @DeleteMapping("/sacarDelista/{idLista}")
    public ResponseEntity<ResponseDTO> eliminarDeLista(
            @Parameter(description = "ID de la lista", required = true)
            @PathVariable Long idLista,

            @Parameter(description = "Nombre del contenido", required = true)
            @RequestParam String nombre
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.eliminarContenido(idLista, nombre);

        return ResponseEntity.ok(new ResponseDTO("Contenido eliminado!"));
    }

    // ------------------ Eliminar lista completa

    @Operation(
            summary = "Eliminar una lista",
            description = "Elimina por completo una lista de un usuario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista eliminada exitosamente")
    })
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR')")
    @DeleteMapping("/eliminarLista")
    public ResponseEntity<ResponseDTO> eliminarLista(
            @Parameter(description = "Nombre de la lista a eliminar", required = true)
            @RequestParam String nombre
    ) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        listasService.eliminarLista(usuarioAutenticado.getId(), nombre);

        return ResponseEntity.ok(new ResponseDTO("Lista Eliminada!"));
    }


    // ------------------ Ver listas de otro usuario

    @Operation(summary = "Ver listas públicas de otro usuario",
            description = "Devuelve las listas públicas de un usuario específico por su nombre de usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listas obtenidas correctamente")
    })
    @Parameter(name = "username", description = "Nombre de usuario", required = true)
    @PreAuthorize("hasAuthority('LISTA_VER_PUBLICAS')")
    @GetMapping("/verListasDeUser/{username}")
    public ResponseEntity<Page<ListaContenidoDTO>> verListas(
            @PathVariable("username") String username,
            Pageable pageable
    ) {
        return ResponseEntity.ok(listasService.verListaDeUser(username, pageable));
    }


}
