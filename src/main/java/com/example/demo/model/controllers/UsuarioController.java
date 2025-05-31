package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve una lista paginada de todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })

    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioDTO>> obtenerListaUsuarios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @Operation(summary = "Listar usuarios paginados (custom)", description = "Obtiene una página de usuarios mediante método personalizado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página obtenida correctamente")
    })

    @GetMapping("/listarPaginado")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuariosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> pagina = usuarioService.obtenerUsuariosPaginados(pageable);
        return ResponseEntity.ok(pagina);
    }

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    })

    // agregar listar por filtros

    @PostMapping("/registrar")
    public ResponseEntity<Void> agregarUsuario(@RequestBody UsuarioEntity u) {
        usuarioService.save(u);
        /*return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado)*/
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @Operation(summary = "Ver usuario por ID", description = "Devuelve los datos de un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })



    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioEntity> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioEntity usuarioActualizado) {
        UsuarioEntity actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Reactivar usuario", description = "Activa el estado de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @PatchMapping("/reactivar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    @Operation(summary = "Dar de baja usuario", description = "Desactiva el estado de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @PatchMapping("/darDeBaja/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, false);
        return ResponseEntity.ok("Usuario desactivado.");
    }

//    @PostMapping("/{idUsuario}/like/{idContenido}")
//    public ResponseEntity<String> darLike(@PathVariable long idUsuario, @PathVariable long idContenido){
//        usuarioService.darLike(idUsuario,idContenido);
//        return ResponseEntity.ok("Like guardado");
//    }
//
//    @DeleteMapping("/{idUsuario}/like/{idContenido}")
//    public ResponseEntity<String> quitarLike(@PathVariable long idUsuario, @PathVariable long idContenido){
//        usuarioService.quitarLike(idUsuario,idContenido);
//        return ResponseEntity.ok("Like eliminado");
//    }

    @Operation(
            summary = "Obtener contenido con 'like' de un usuario",
            description = "Devuelve una lista paginada del contenido que el usuario ha marcado como 'me gusta'"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido con like recuperado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })

    @GetMapping("/{idUsuario}/likes")
    public ResponseEntity<Page<ContenidoEntity>> obtenerLikes(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @PathVariable Long idUsuario){
       Pageable pageable = PageRequest.of(page, size);
       Page<ContenidoEntity> pagina = usuarioService.obtenerLikes(idUsuario, pageable);
       return ResponseEntity.ok(pagina);
    }


}
