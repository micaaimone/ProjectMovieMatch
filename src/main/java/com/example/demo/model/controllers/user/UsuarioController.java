package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
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

    //deberia hacerlo el specific
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

    @PostMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> darLike(@PathVariable Long idUsuario, @PathVariable Long idContenido){
        usuarioService.darLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like guardado");
    }

    @DeleteMapping("/{idUsuario}/like/{idContenido}")
    public ResponseEntity<String> quitarLike(@PathVariable Long idUsuario, @PathVariable Long idContenido){
        usuarioService.quitarLike(idUsuario,idContenido);
        return ResponseEntity.ok("Like eliminado");
    }


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
