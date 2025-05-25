package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/listar")
    public List<UsuarioEntity> obtenerListaUsuarios(){
        return usuarioService.findAll();
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
    public ResponseEntity<UsuarioEntity> agregarUsuario(@Valid @RequestBody UsuarioEntity u) {
        UsuarioEntity usuarioGuardado = usuarioService.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado con éxito.");
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        UsuarioDTO dto = usuarioService.getUsuarioDTO(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioEntity> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioEntity usuarioActualizado) {
        UsuarioEntity actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    @PatchMapping("/desactivar/{id}")
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
    public ResponseEntity<Set<ContenidoEntity>> obtenerLikes(@PathVariable Long idUsuario){
        return ResponseEntity.ok(usuarioService.listarLikes(idUsuario));
    }


}
