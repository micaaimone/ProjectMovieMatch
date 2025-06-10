package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.ContenidoLike;
import com.example.demo.model.entities.User.ReseniaLike;
import com.example.demo.model.services.Usuarios.ContenidoLikeService;
import com.example.demo.model.services.Usuarios.ReseniaLikeService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import jakarta.validation.Valid;
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
    private final ContenidoLikeService contenidoLikeService;
    private final ReseniaLikeService reseniaLikeService;

    public UsuarioController(UsuarioService usuarioService, ContenidoLikeService contenidoLikeService, ReseniaLikeService reseniaLikeService) {
        this.usuarioService = usuarioService;
        this.contenidoLikeService = contenidoLikeService;
        this.reseniaLikeService = reseniaLikeService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> agregarUsuario(@Valid @RequestBody NewUsuarioDTO u) {
        usuarioService.save(u);
        return ResponseEntity.ok("Usuario creado con exito.");
    }


    //deberia hacerlo el specific
    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<String > actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModificarDTO usuarioActualizado) {
        usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    @PatchMapping("/reactivar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    @PatchMapping("/darDeBaja/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, false);
        return ResponseEntity.ok("Usuario desactivado.");
    }

    @PostMapping("/{usuarioId}/like/contenido/{contenidoId}")
    public ResponseEntity<String> likeContenido(
            @PathVariable Long usuarioId,
            @PathVariable Long contenidoId) {

        contenidoLikeService.darLike(usuarioId, contenidoId);
        return ResponseEntity.ok("Like a contenido registrado");
    }

    @DeleteMapping("/{usuarioId}/like/contenido/{contenidoId}")
    public ResponseEntity<String> quitarLikeContenido(
            @PathVariable Long usuarioId,
            @PathVariable Long contenidoId) {

        boolean eliminado = reseniaLikeService.quitarLike(usuarioId, contenidoId);
        if (eliminado) {
            return ResponseEntity.ok("Like a contenido eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el like para eliminar");
        }
    }

    @GetMapping("/{usuarioId}/likes/contenido")
    public ResponseEntity<Page<ContenidoLike>> getContenidoLikes(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ContenidoLike> likes = contenidoLikeService.obtenerLikes(usuarioId, page, size);
        return ResponseEntity.ok(likes);
    }

    @PostMapping("/{usuarioId}/like/resenia/{reseniaId}")
    public ResponseEntity<String> likeResenia(
            @PathVariable Long usuarioId,
            @PathVariable Long reseniaId) {

        reseniaLikeService.darLike(usuarioId, reseniaId);
        return ResponseEntity.ok("Like a reseña registrado");
    }

    @DeleteMapping("/{usuarioId}/like/resenia/{reseniaId}")
    public ResponseEntity<String> quitarLikeResenia(
            @PathVariable Long usuarioId,
            @PathVariable Long reseniaId) {

        boolean eliminado = reseniaLikeService.quitarLike(usuarioId, reseniaId);
        if (eliminado) {
            return ResponseEntity.ok("Like a reseña eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el like para eliminar");
        }
    }

    @GetMapping("/{usuarioId}/likes/resenia")
    public ResponseEntity<Page<ReseniaLike>> getReseniaLikes(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ReseniaLike> likes = reseniaLikeService.obtenerLikes(usuarioId, page, size);
        return ResponseEntity.ok(likes);
    }

    //el dto no tiene nombre, apellido, ni id. deberia?
    //cambiar el dto de contenido para solo mostrar titulo
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
