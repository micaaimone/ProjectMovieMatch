package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ------------------- Registro de usuario
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/registrar")
    public ResponseEntity<String> agregarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewUsuarioDTO.class))
            )
            @Valid @RequestBody NewUsuarioDTO u) {
        usuarioService.save(u);
        return ResponseEntity.ok("Usuario creado con exito.");
    }

    // ------------------- Ver usuario por ID
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve los datos del usuario correspondiente al ID proporcionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PreAuthorize("hasAuthority('USUARIO_VER')")
    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(
            @Parameter(description = "ID del usuario a consultar", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    // ------------------- Actualizar datos
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PreAuthorize("hasAuthority('USUARIO_MODIFICAR')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String> actualizarUsuario(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos a modificar del usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioModificarDTO.class))
            )
            @Valid @RequestBody UsuarioModificarDTO usuarioActualizado) {
        usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    // ------------------- Activar / desactivar
    @Operation(summary = "Activar usuario", description = "Activa el estado de un usuario previamente desactivado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario activado correctamente")
    })
    @PreAuthorize("hasAuthority('USUARIO_REACTIVAR')")
    @PatchMapping("/reactivar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    @Operation(summary = "Desactivar usuario", description = "Desactiva el usuario, dejándolo inhabilitado en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario desactivado correctamente")
    })
    @PatchMapping("/darDeBaja/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, false);
        return ResponseEntity.ok("Usuario desactivado.");
    }

    // ------------------- Likes
    @Operation(summary = "Dar like a contenido", description = "Registra un 'me gusta' de un usuario sobre un contenido específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Like registrado correctamente")
    })
    @PreAuthorize("hasAuthority('USUARIO_LIKE')")
    @PostMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> darLike(@PathVariable Long idUsuario, @PathVariable Long idContenido) {
        usuarioService.darLike(idUsuario, idContenido);
        return ResponseEntity.ok("Like guardado");
    }

    @Operation(summary = "Quitar like", description = "Elimina un 'me gusta' de un contenido dado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Like eliminado correctamente")
    })
    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> quitarLike(@PathVariable Long idUsuario, @PathVariable Long idContenido) {
        usuarioService.quitarLike(idUsuario, idContenido);
        return ResponseEntity.ok("Like eliminado");
    }

    @Operation(summary = "Ver contenido con like", description = "Devuelve una lista paginada de los contenidos a los que el usuario dio like.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de likes obtenido correctamente")
    })
    @Parameters({
            @Parameter(name = "page", description = "Número de página", schema = @Schema(defaultValue = "0")),
            @Parameter(name = "size", description = "Cantidad de resultados por página", schema = @Schema(defaultValue = "10"))
    })
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/{idUsuario}/likes")
    public ResponseEntity<Page<ContenidoEntity>> obtenerLikes(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @PathVariable Long idUsuario) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContenidoEntity> pagina = usuarioService.obtenerLikes(idUsuario, pageable);
        return ResponseEntity.ok(pagina);
    }

    // ------------------- Listar usuarios activos / desactivados
    @Operation(summary = "Listar usuarios activos", description = "Filtra y devuelve usuarios activos según filtros opcionales.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de usuarios activos")
    })
    @PreAuthorize("hasAuthority('USUARIO_LISTAR')")
    @GetMapping("/listarActivos")
    public ResponseEntity<Page<UsuarioDTO>> filtrarUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            Pageable pageable) {
        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(nombre, apellido, email, username, true, pageable);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Listar usuarios desactivados", description = "Filtra y devuelve usuarios que están desactivados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de usuarios desactivados")
    })
    @PreAuthorize("hasAuthority('USUARIO_LISTAR_DESACTIVADOS')")
    @GetMapping("/listarDesactivados")
    public ResponseEntity<Page<UsuarioDTO>> filtrarUsuariosDesactivados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            Pageable pageable) {
        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(nombre, apellido, email, username, false, pageable);
        return ResponseEntity.ok(resultado);
    }

}
