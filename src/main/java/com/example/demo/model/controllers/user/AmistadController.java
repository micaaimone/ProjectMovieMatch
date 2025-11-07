package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.Amistad.AmigoDTO;
import com.example.demo.model.DTOs.Amistad.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.Amistad.SolicitudAmistadDTO;
import com.example.demo.model.DTOs.ResponseDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Usuarios.AmistadService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios/amistades")
public class AmistadController {

    private final AmistadService solicitudAmistadService;
    private final UsuarioService usuarioService;

    @Autowired
    public AmistadController(AmistadService solicitudAmistadService, UsuarioService usuarioService) {
        this.solicitudAmistadService = solicitudAmistadService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Enviar solicitud de amistad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud enviada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/enviarSolicitud")
    public ResponseEntity<ResponseDTO> enviarSolicitud(@Valid @RequestBody NewSolicitudAmistadDTO s) {
        UsuarioEntity user = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.save(s, user.getId());
        return ResponseEntity.ok(new ResponseDTO("Solicitud enviada con exito."));
    }

    @Operation(summary = "Aceptar solicitud de amistad")
    @ApiResponse(responseCode = "200", description = "Solicitud aceptada")
    @PostMapping("/aceptarSolicitud/{idEmisor}")
    public ResponseEntity<ResponseDTO> aceptarSolicitud(@PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.aceptarSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ResponseDTO("Solicitud aceptada"));
    }

    @Operation(summary = "Rechazar solicitud de amistad")
    @ApiResponse(responseCode = "200", description = "Solicitud rechazada")
    @PostMapping("/rechazarSolicitud/{idEmisor}")
    public ResponseEntity<ResponseDTO> rechazarSolicitud(@PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.rechazarSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ResponseDTO("Solicitud rechazada"));
    }

    @Operation(summary = "Bloquear solicitud de amistad")
    @ApiResponse(responseCode = "200", description = "Solicitud bloqueada")
    @PostMapping("/bloquear/{idEmisor}")
    public ResponseEntity<ResponseDTO> bloquear(@PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.bloquearSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok(new ResponseDTO("Solicitud bloqueada"));
    }

    @Operation(summary = "Listar solicitudes enviadas")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes recibidas")
    @GetMapping
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarPorID(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.listarMisSolicitudes(usuarioAutenticado.getId(), pageable));
    }

    @Operation(summary = "Ver una solicitud en particular")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @GetMapping("/solicitud/{idReceptor}")
    public ResponseEntity<SolicitudAmistadDTO> verSolicitud(@PathVariable Long idReceptor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.obtenerSolicitud(usuarioAutenticado.getId(), idReceptor));
    }

    @Operation(summary = "Cancelar solicitud de amistad enviada")
    @ApiResponse(responseCode = "204", description = "Solicitud cancelada")
    @DeleteMapping("/cancelar/{idReceptor}")
    public ResponseEntity<Void> cancelarSolicitud(@PathVariable Long idReceptor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.cancelarSolicitud(usuarioAutenticado.getId(), idReceptor);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar mis solicitudes pendientes")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes pendientes")
    @GetMapping("/pendientesPropias")
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarSolicitudesPendientes(@RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.listarSolicitudesPendientes(usuarioAutenticado.getId(), pageable));
    }

    @Operation(summary = "Listar mis amigos")
    @ApiResponse(responseCode = "200", description = "Lista de amigos")
    @GetMapping("/misAmigos")
    public ResponseEntity<Page<AmigoDTO>> listarAmigosMios(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.mostrarMisAmigos(usuarioAutenticado.getId(), pageable));
    }

    @Operation(summary = "Eliminar amigo")
    @ApiResponse(responseCode = "200", description = "Amigo eliminado")
    @DeleteMapping("/amigos/{idAmigo}")
    public ResponseEntity<ResponseDTO> eliminarAmigo(@PathVariable Long idAmigo) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.eliminarAmigo(usuarioAutenticado.getId(), idAmigo);
        return ResponseEntity.ok(new ResponseDTO("Amigo eliminado"));
    }

    @Operation(summary = "Visualizar coincidencias de contenido con un amigo")
    @ApiResponse(responseCode = "200", description = "Coincidencias encontradas")
    @GetMapping("/coincidencias/{idReceptor}")
    public ResponseEntity<Page<ContenidoMostrarDTO>> visualizarCoincidencias(@PathVariable Long idReceptor,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Page<ContenidoMostrarDTO> coincidencias = solicitudAmistadService.visualizarCoincidencias(usuarioAutenticado.getId(), idReceptor, pageable);
        return ResponseEntity.ok(coincidencias);
    }
}
