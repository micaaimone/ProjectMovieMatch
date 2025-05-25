package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.UsuarioDTO;
import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
    // manejar excepciones y usar dtos

    @GetMapping("/listar")
    public List<UsuarioEntity> obtenerListaUsuarios(){
        return usuarioService.findAll();
    }

    // cambiar nombre
    @GetMapping("/listar")
    public List<UsuarioDTO> obtenerListaDTOs(){
        return usuarioService.getAllDTO();
    }

    @PostMapping("/registrar")
    public void agregarUsuario(@RequestParam UsuarioEntity u){
        usuarioService.save(u);
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuario(@RequestParam long id){

        return usuarioService.getUsuarioDTO(id);
    }


    @PostMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> darLike(@PathVariable long idUsuario, @PathVariable long idContenido){
        usuarioService.darLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like guardado");
    }

    @DeleteMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> quitarLike(@PathVariable long idUsuario, @PathVariable long idContenido){
        usuarioService.quitarLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like eliminado");
    }


    @GetMapping("/{usuarioId}/likes")
    public ResponseEntity<Set<ContenidoEntity>> obtenerLikes(@PathVariable Long idUsuario){
        return ResponseEntity.ok(usuarioService.listarLikes(idUsuario));
    }


    // usar dto?
//    @GetMapping("/usuarios/{id}")
//    public EntityModel<UsuarioDTO> obtenerUsuarioPerfil(@PathVariable Long id){
//        UsuarioDTO usuario = usuarioService.getUsuarioDTO(id);
//                //.orElseThrow(()-> new RuntimeException("error"));
//
//
//    }


}
