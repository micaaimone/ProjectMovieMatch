package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.services.Usuarios.UsuarioService;
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

    //usar specifications

    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioDTO>> obtenerListaUsuarios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    @GetMapping("/listarPaginado")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuariosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> pagina = usuarioService.obtenerUsuariosPaginados(pageable);
        return ResponseEntity.ok(pagina);
    }

    // agregar listar por filtros

    @PostMapping("/registrar")
    public ResponseEntity<Void> agregarUsuario(@RequestBody UsuarioEntity u) {
        usuarioService.save(u);
        /*return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado)*/
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioEntity> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioEntity usuarioActualizado) {
        UsuarioEntity actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(actualizado);
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


    @GetMapping("/{idUsuario}/likes")
    public ResponseEntity<Page<ContenidoEntity>> obtenerLikes(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @PathVariable Long idUsuario){
       Pageable pageable = PageRequest.of(page, size);
       Page<ContenidoEntity> pagina = usuarioService.obtenerLikes(idUsuario, pageable);
       return ResponseEntity.ok(pagina);
    }


}
