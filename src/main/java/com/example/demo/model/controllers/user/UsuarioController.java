package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.MailDTO;
import com.example.demo.model.DTOs.user.NewUsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Email.EmailService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public UsuarioController(UsuarioService usuarioService, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    // ------------------- Registro de usuario
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/registrar")
    public ResponseEntity<String> agregarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewUsuarioDTO.class))
            )
            @Valid @RequestBody NewUsuarioDTO u) {
        usuarioService.save(u);
        return ResponseEntity.ok("Usuario creado con exito.");
    }

    // ------------------- Ver usuario por ID
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve los datos del usuario correspondiente al ID proporcionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PreAuthorize("hasAuthority('USUARIO_VER')")
    @GetMapping("/ver/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(
            @Parameter(description = "ID del usuario a consultar", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Ver mi perfil")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/miPerfil")
    public ResponseEntity<UsuarioDTO> verMiPerfil() {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        return ResponseEntity.ok(usuarioService.mostrarMiPerfil(usuarioAutenticado));
    }

    // ------------------- Actualizar datos
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/modificar")
    public ResponseEntity<String> modificarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos a modificar del usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioModificarDTO.class))
            )
            @Valid @RequestBody UsuarioModificarDTO usuarioActualizado) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        usuarioService.actualizarUsuario(usuarioAutenticado.getId(), usuarioActualizado);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    @Operation(summary = "Desactivar usuario")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/darmeDeBaja")
    public ResponseEntity<String> desactivarMiUsuario() {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        usuarioService.cambiarEstadoUsuario(usuarioAutenticado.getId(), false);
        return ResponseEntity.ok("Usuario desactivado.");
    }



    //estos son metodos que solo el admin puede hacer
    // ------------------- Activar / desactivar usuario
    @Operation(summary = "Activar usuario")
    @PreAuthorize("hasAuthority('USUARIO_REACTIVAR')")
    @PatchMapping("/reactivar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstadoUsuario(id, true);
        return ResponseEntity.ok("Usuario activado.");
    }

    // ------------------- Listar usuarios activos / desactivados
    @Operation(summary = "Listar usuarios activos")
    @PreAuthorize("hasAuthority('USUARIO_LISTAR')")
    @GetMapping("/listarActivos")
    public ResponseEntity<Page<UsuarioDTO>> filtrarUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            Pageable pageable) {
        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(nombre, apellido, email, username, true, pageable);
        return ResponseEntity.ok(resultado);
    }

    //mail---------------------------------

    //recibimos un mail de queja de un usuario
    @Operation(summary = "Enviar un mail a soporte")
    @PreAuthorize("hasAuthority('USUARIO_SOLICITAR_SOPORTE')")
    @PostMapping("/soporte")
    public ResponseEntity<String> soporteUsuario( @Valid @RequestBody MailDTO mailDTO) {

        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        usuarioService.soporte(usuarioAutenticado.getId(), mailDTO);

        return ResponseEntity.ok("Mail enviado al soporte");
    }

    //enviamos a todos los usuarios activos un mail de aviso de x cosa
    @Operation(summary = "Enviar anuncio a los usuaarios")
    @PreAuthorize("hasAuthority('USUARIO_ENVIAR_ANUNCIO')")
    @PostMapping("/anuncio")
    public ResponseEntity<String> anuncioUsuario(@Valid @RequestBody MailDTO mailDTO) {
        emailService.SendMailToAll(mailDTO);
        return ResponseEntity.ok("Anuncio enviado a los usuarios");
    }

}
