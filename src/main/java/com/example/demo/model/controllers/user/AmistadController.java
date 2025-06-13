package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.user.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.user.SolicitudAmistadDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Usuarios.AmistadService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/amistades")
public class AmistadController {
    private final AmistadService solicitudAmistadService;
    //uso el service unicamente por la autenticacion
    private final UsuarioService usuarioService;

    @Autowired
    public AmistadController(AmistadService solicitudAmistadService, UsuarioService usuarioService) {
        this.solicitudAmistadService = solicitudAmistadService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/enviarSolicitud")
    public ResponseEntity<String> enviarSolicitud(@Valid @RequestBody NewSolicitudAmistadDTO s)
    {
        solicitudAmistadService.save(s);
        return ResponseEntity.ok("Solicitud enviada con exito.");
    }

    @PostMapping("/aceptarSolicitud/{idEmisor}")
    public ResponseEntity<String> aceptarSolicitud( @PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.aceptarSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok("Solicitud aceptada");
    }

    @PostMapping("/rechazarSolicitud/{idEmisor}")
    public ResponseEntity<String> rechazarSolicitud(@PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.rechazarSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok("Solicitud rechazada");
    }

    @PostMapping("/bloquear/{idEmisor}")
    public ResponseEntity<String> bloquear(@PathVariable Long idEmisor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.bloquearSolicitud(idEmisor, usuarioAutenticado.getId());
        return ResponseEntity.ok("Solicitud bloqueada");
    }

    @GetMapping
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarPorID(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.listarMisSolicitudes(usuarioAutenticado.getId(), pageable));
    }

    @GetMapping("/solicitud/{idReceptor}")
    public ResponseEntity<SolicitudAmistadDTO> verSolicitud(
            @PathVariable Long idReceptor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.obtenerSolicitud(usuarioAutenticado.getId(), idReceptor));
    }

    @DeleteMapping("/cancelar/{idReceptor}")
    public ResponseEntity<Void> cancelarSolicitud( @PathVariable Long idReceptor) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.cancelarSolicitud(usuarioAutenticado.getId(), idReceptor);
        return ResponseEntity.noContent().build();
    }


    //cambiar, falta q  aparezca bn el emisor
    @GetMapping("/pendientes")
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarSolicitudesPendientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(solicitudAmistadService.listarSolicitudesPendientes(usuarioAutenticado.getId(), pageable));
    }

    @DeleteMapping("/{idUsuario}/amigos/{idAmigo}")
    public ResponseEntity<?> eliminarAmigo(@PathVariable Long idAmigo) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        solicitudAmistadService.eliminarAmigo(usuarioAutenticado.getId(), idAmigo);
        return ResponseEntity.ok("Amigo eliminado");
    }

    @GetMapping("/coincidencias/{idReceptor}")
    public ResponseEntity<Page<ContenidoMostrarDTO>> visualizarCoincidencias(
            @PathVariable Long idReceptor,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Page<ContenidoMostrarDTO> coincidencias = solicitudAmistadService.visualizarCoincidencias(usuarioAutenticado.getId(), idReceptor, pageable);
        return ResponseEntity.ok(coincidencias);
    }


}
