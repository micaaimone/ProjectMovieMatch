package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.entities.User.ContenidoLike;
import com.example.demo.model.entities.User.ReseniaLike;
import com.example.demo.model.services.Usuarios.ContenidoLikeService;
import com.example.demo.model.services.Usuarios.ReseniaLikeService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/like")
public class LikeController {

    private final UsuarioService usuarioService;
    private final ContenidoLikeService contenidoLikeService;
    private final ReseniaLikeService reseniaLikeService;

    @Autowired
    public LikeController(UsuarioService usuarioService, ContenidoLikeService contenidoLikeService, ReseniaLikeService reseniaLikeService) {
        this.usuarioService = usuarioService;
        this.contenidoLikeService = contenidoLikeService;
        this.reseniaLikeService = reseniaLikeService;
    }

    // ------------------- Likes a contenido
    @Operation(summary = "Dar like a contenido")
    @PreAuthorize("hasAuthority('USUARIO_LIKE')")
    @PostMapping("/{usuarioId}/like/contenido/{contenidoId}")
    public ResponseEntity<String> likeContenido(@PathVariable Long usuarioId, @PathVariable Long contenidoId) {
        contenidoLikeService.darLike(usuarioId, contenidoId);
        return ResponseEntity.ok("Like a contenido registrado");
    }

    @Operation(summary = "Quitar like a contenido")
    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/{usuarioId}/like/contenido/{contenidoId}")
    public ResponseEntity<String> quitarLikeContenido(@PathVariable Long usuarioId, @PathVariable Long contenidoId) {
        boolean eliminado = contenidoLikeService.quitarLike(usuarioId, contenidoId);
        if (eliminado) {
            return ResponseEntity.ok("Like a contenido eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el like para eliminar");
        }
    }

    @Operation(summary = "Ver likes de contenido")
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/{usuarioId}/likes/contenido")
    public ResponseEntity<Page<ContenidoLike>> getContenidoLikes(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ContenidoLike> likes = contenidoLikeService.obtenerLikes(usuarioId, page, size);
        return ResponseEntity.ok(likes);
    }

    // ------------------- Likes a reseñas
    @Operation(summary = "Dar like a reseña")
    @PreAuthorize("hasAuthority('USUARIO_LIKE')")
    @PostMapping("/{usuarioId}/like/resenia/{reseniaId}")
    public ResponseEntity<String> likeResenia(@PathVariable Long usuarioId, @PathVariable Long reseniaId) {
        reseniaLikeService.darLike(usuarioId, reseniaId);
        return ResponseEntity.ok("Like a reseña registrado");
    }

    @Operation(summary = "Quitar like a reseña")
    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/{usuarioId}/like/resenia/{reseniaId}")
    public ResponseEntity<String> quitarLikeResenia(@PathVariable Long usuarioId, @PathVariable Long reseniaId) {
        boolean eliminado = reseniaLikeService.quitarLike(usuarioId, reseniaId);
        if (eliminado) {
            return ResponseEntity.ok("Like a reseña eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el like para eliminar");
        }
    }

    @Operation(summary = "Ver likes de reseñas")
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/{usuarioId}/likes/resenia")
    public ResponseEntity<Page<ReseniaLike>> getReseniaLikes(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReseniaLike> likes = reseniaLikeService.obtenerLikes(usuarioId, page, size);
        return ResponseEntity.ok(likes);
    }

    // ------------------- Listar contenido con likes
    @Operation(summary = "Ver contenidos con like")
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/{idUsuario}/likes")
    public ResponseEntity<Page<ContenidoMostrarDTO>> obtenerLikes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long idUsuario) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContenidoMostrarDTO> pagina = usuarioService.obtenerLikes(idUsuario, pageable);
        return ResponseEntity.ok(pagina);
    }
}
