package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.services.Usuarios.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ------------------- Registro de usuario
    @PreAuthorize("permitAll()")
    @PostMapping("/registrar")
    public ResponseEntity<String> agregarUsuario(@Valid @RequestBody NewUsuarioDTO u) {
        usuarioService.save(u);
        return ResponseEntity.ok("Usuario creado con exito.");
    }


    // ------------------- Ver usuario por ID
    @PreAuthorize("hasAuthority('USUARIO_VER')")
    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    // ------------------- Actualizar datos
    @PreAuthorize("hasAuthority('USUARIO_MODIFICAR')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String > actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModificarDTO usuarioActualizado) {
        usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    // ------------------- Activar / desactivar
    @PreAuthorize("hasAuthority('USUARIO_REACTIVAR')")
    @PatchMapping("/reactivar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    @PreAuthorize("hasAuthority('USUARIO_DESACTIVAR')")
    @PatchMapping("/darDeBaja/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, false);
        return ResponseEntity.ok("Usuario desactivado.");
    }

    // ------------------- Likes
    @PreAuthorize("hasAuthority('USUARIO_LIKE')")
    @PostMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> darLike(@PathVariable Long idUsuario, @PathVariable Long idContenido){
        usuarioService.darLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like guardado");
    }

    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> quitarLike(@PathVariable Long idUsuario, @PathVariable Long idContenido){
        usuarioService.quitarLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like eliminado");
    }

    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/{idUsuario}/likes")
    public ResponseEntity<Page<ContenidoEntity>> obtenerLikes(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @PathVariable Long idUsuario){
       Pageable pageable = PageRequest.of(page, size);
       Page<ContenidoEntity> pagina = usuarioService.obtenerLikes(idUsuario, pageable);
       return ResponseEntity.ok(pagina);
    }

    //el dto no tiene nombre, apellido, ni id. deberia?
    //cambiar el dto de contenido para solo mostrar titulo
    // ------------------- Listar usuarios activos / desactivados
    @PreAuthorize("hasAuthority('USUARIO_LISTAR')")
    @GetMapping("/listarActivos")
    public ResponseEntity<Page<UsuarioDTO>> filtrarUsuarios(@RequestParam(required = false) String nombre,
                                               @RequestParam(required = false) String apellido,
                                               @RequestParam(required = false) String email,
                                               @RequestParam(required = false) String username,
                                               Pageable pageable){
        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(nombre, apellido, email, username, true, pageable);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/listarDesactivados")
    public ResponseEntity<Page<UsuarioDTO>> filtrarUsuariosDesactivados(@RequestParam(required = false) String nombre,
                                                            @RequestParam(required = false) String apellido,
                                                            @RequestParam(required = false) String email,
                                                            @RequestParam(required = false) String username,
                                                            Pageable pageable){
        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(nombre, apellido, email, username, false, pageable);

        return ResponseEntity.ok(resultado);
    }

}
