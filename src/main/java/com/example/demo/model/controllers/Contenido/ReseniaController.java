package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaModificarDTO;
import com.example.demo.model.services.Contenido.ReseniaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resenia")
public class ReseniaController {

    private final ReseniaService reseniaService;

    @Autowired
    public ReseniaController(ReseniaService reseniaService) {
        this.reseniaService = reseniaService;
    }

    @PreAuthorize("hasAuthority('CREAR_RESENIA')")
    @PostMapping
    public ResponseEntity<String> crearResenia(@Valid @RequestBody ReseniaDTO reseniaDTO){
        reseniaService.save(reseniaDTO);
        return ResponseEntity.ok("Reseña agregada correctamente");
    }

    @PreAuthorize("hasAuthority('ELIMINAR_RESENIA')")
    @DeleteMapping("/eliminarReseniaaByID/{id}")
    public ResponseEntity<String> eliminarReseniaPorId(@PathVariable("id") Long id)
    {
        reseniaService.delete(id);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('ELIMINAR_RESENIA')")
    @DeleteMapping("/eliminarReseniaByUserAndIDContenido{id_usuario}/{id_contenido}")
    public ResponseEntity<String> eliminarReseniaByUsuarioYContenido(@PathVariable("id_usuario") Long id_usuario,
                                            @PathVariable("id_contenido") Long id_contenido)
    {
        reseniaService.delete(id_usuario, id_contenido);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('VER_RESENIAS')")
    @GetMapping("/{id_usuario}")
    public ResponseEntity<Page<ReseniaDTO>> listarPorUsuario(@PathVariable("id_usuario") Long id_usuario,
                                                             @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reseniaService.listarReseniasPorUsuario(id_usuario, pageable));
    }

    @PreAuthorize("hasAuthority('MODIFICAR_RESENIA')")
    @PatchMapping("/{id_usuario}/{id_contenido}")
    public ResponseEntity<String> modificar (@PathVariable("id_usuario") Long id_usuario,
                                        @PathVariable("id_contenido") Long id_contenido,
                                             @Valid @RequestBody ReseniaModificarDTO dto)
    {
        reseniaService.modificarResenia(id_usuario, id_contenido, dto);

        return ResponseEntity.ok("Reseña modificada correctamente");
    }


}
