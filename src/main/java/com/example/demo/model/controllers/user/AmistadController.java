package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.user.NewSolicitudAmistadDTO;
import com.example.demo.model.DTOs.user.SolicitudAmistadDTO;
import com.example.demo.model.DTOs.user.UsuarioModificarDTO;
import com.example.demo.model.services.Usuarios.AmistadService;
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

    @Autowired
    public AmistadController(AmistadService solicitudAmistadService) {
        this.solicitudAmistadService = solicitudAmistadService;
    }

    @PostMapping("/enviarSolicitud")
    public ResponseEntity<String> enviarSolicitud(@Valid @RequestBody NewSolicitudAmistadDTO s)
    {
        solicitudAmistadService.save(s);
        return ResponseEntity.ok("Solicitud enviada con exito.");
    }

    @PostMapping("/{idReceptor}/aceptarSolicitud/{idEmisor}")
    public ResponseEntity<String> aceptarSolicitud(@PathVariable Long idReceptor, @PathVariable Long idEmisor) {
        solicitudAmistadService.aceptarSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud aceptada");
    }

    @PostMapping("/{idReceptor}/rechazarSolicitud/{idEmisor}")
    public ResponseEntity<String> rechazarSolicitud(@PathVariable Long idReceptor, @PathVariable Long idEmisor) {
        solicitudAmistadService.rechazarSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud rechazada");
    }

    @PostMapping("/{idReceptor}/bloquear/{idEmisor}")
    public ResponseEntity<String> bloquear(@PathVariable Long idReceptor, @PathVariable Long idEmisor) {
        solicitudAmistadService.bloquearSolicitud(idEmisor, idReceptor);
        return ResponseEntity.ok("Solicitud bloqueada");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarPorID(@PathVariable("id") Long idEmisor,
                                                                 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(solicitudAmistadService.listarMisSolicitudes(idEmisor, pageable));
    }

    @GetMapping("/{idEmisor}/solicitud/{idReceptor}")
    public ResponseEntity<SolicitudAmistadDTO> verSolicitud(
            @PathVariable Long idEmisor,
            @PathVariable Long idReceptor) {
        return ResponseEntity.ok(solicitudAmistadService.obtenerSolicitud(idEmisor, idReceptor));
    }

    @DeleteMapping("/{idEmisor}/cancelar/{idReceptor}")
    public ResponseEntity<Void> cancelarSolicitud(@PathVariable Long idEmisor, @PathVariable Long idReceptor) {
        solicitudAmistadService.cancelarSolicitud(idEmisor, idReceptor);
        return ResponseEntity.noContent().build();
    }


//cambiar, falta q  aparezca bn el emisor
    @GetMapping("/{idReceptor}/pendientes")
    public ResponseEntity<Page<SolicitudAmistadDTO>> listarSolicitudesPendientes(
            @PathVariable Long idReceptor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(solicitudAmistadService.listarSolicitudesPendientes(idReceptor, pageable));
    }

    @DeleteMapping("/{idUsuario}/amigos/{idAmigo}")
    public ResponseEntity<?> eliminarAmigo(@PathVariable Long idUsuario, @PathVariable Long idAmigo) {
        solicitudAmistadService.eliminarAmigo(idUsuario, idAmigo);
        return ResponseEntity.ok("Amigo eliminado");
    }

    @GetMapping("/{idEmisor}/coincidencias/{idReceptor}")
    public ResponseEntity<Page<ContenidoMostrarDTO>> visualizarCoincidencias(
            @PathVariable Long idEmisor,
            @PathVariable Long idReceptor,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContenidoMostrarDTO> coincidencias = solicitudAmistadService.visualizarCoincidencias(idEmisor, idReceptor, pageable);
        return ResponseEntity.ok(coincidencias);
    }


}
