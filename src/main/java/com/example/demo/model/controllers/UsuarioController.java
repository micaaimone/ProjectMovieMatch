package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    // cambiar nombre
    @GetMapping("/listarCrudo")
    public List<UsuarioEntity> obtenerListaUsuarios(){
        return usuarioService.findAll();
    }

    @GetMapping("/listar")
    public List<UsuarioDTO> obtenerListaDTOs(){
        return usuarioService.getAllDTO();
    }

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
    public UsuarioDTO obtenerUsuario(@RequestParam long id){

        return usuarioService.getUsuarioDTO(id);
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


    @GetMapping("/{usuarioId}/likes")
    public ResponseEntity<Set<ContenidoEntity>> obtenerLikes(@PathVariable Long idUsuario){
        return ResponseEntity.ok(usuarioService.listarLikes(idUsuario));
    }


}
