package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Resenia.ContenidoLikeDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaLikeDTO;
import com.example.demo.model.DTOs.ResponseDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Usuarios.ContenidoLikeService;
import com.example.demo.model.services.Usuarios.ReseniaLikeService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios/like")
public class LikeController {

    //uso el service unicamente por la autenticacion
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
    @PostMapping("/contenido/{contenidoId}")
    public ResponseEntity<ResponseDTO> likeContenido(@PathVariable Long contenidoId) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        contenidoLikeService.darLike(usuarioAutenticado.getId(), contenidoId);
        return ResponseEntity.ok(new ResponseDTO("Like a contenido registrado"));
    }

    @Operation(summary = "Quitar like a contenido")
    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/contenido/{contenidoId}")
    public ResponseEntity<ResponseDTO> quitarLikeContenido(@PathVariable Long contenidoId) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        boolean eliminado = contenidoLikeService.quitarLike(usuarioAutenticado.getId(), contenidoId);
        if (eliminado) {
            return ResponseEntity.ok(new ResponseDTO("Like a contenido eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("No se encontró el like para eliminar"));
        }
    }

    @Operation(summary = "Ver likes de contenido")
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/contenidosLikeados")
    public ResponseEntity<Page<ContenidoLikeDTO>> getContenidoLikes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Page<ContenidoLikeDTO> likes = contenidoLikeService.obtenerLikes(usuarioAutenticado.getId(), page, size);
        return ResponseEntity.ok(likes);
    }

    // ------------------- Likes a reseñas
    @Operation(summary = "Dar like a reseña")
    @PreAuthorize("hasAuthority('USUARIO_LIKE')")
    @PostMapping("/resenia/{reseniaId}")
    public ResponseEntity<ResponseDTO> likeResenia(@PathVariable Long reseniaId) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        reseniaLikeService.darLike(usuarioAutenticado.getId(), reseniaId);
        return ResponseEntity.ok(new ResponseDTO("Like a reseña registrado"));
    }

    @Operation(summary = "Quitar like a reseña")
    @PreAuthorize("hasAuthority('USUARIO_QUITAR_LIKE')")
    @DeleteMapping("/resenia/{reseniaId}")
    public ResponseEntity<ResponseDTO> quitarLikeResenia( @PathVariable Long reseniaId) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        boolean eliminado = reseniaLikeService.quitarLike(usuarioAutenticado.getId(), reseniaId);
        if (eliminado) {
            return ResponseEntity.ok(new ResponseDTO("Like a reseña eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("No se encontró el like para eliminar"));
        }
    }

    //modificar, tiene que devolver un dto
    @Operation(summary = "Ver likes de reseñas")
    @PreAuthorize("hasAuthority('USUARIO_VER_LIKES')")
    @GetMapping("/reseniasLikeadas")
    public ResponseEntity<Page<ReseniaLikeDTO>> getReseniaLikes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Page<ReseniaLikeDTO> likes = reseniaLikeService.obtenerLikes(usuarioAutenticado.getId(), page, size);
        return ResponseEntity.ok(likes);
    }
}
