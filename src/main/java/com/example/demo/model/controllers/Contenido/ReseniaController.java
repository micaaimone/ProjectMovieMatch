package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaModificarDTO;
import com.example.demo.model.services.Contenido.ReseniaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear reseña", description = "Permite agregar una nueva reseña a un contenido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña agregada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    @PostMapping
    public ResponseEntity<String> crearResenia(@Valid @RequestBody ReseniaDTO reseniaDTO){
        reseniaService.save(reseniaDTO);
        return ResponseEntity.ok("Reseña agregada correctamente");
    }

    @PreAuthorize("hasAuthority('ELIMINAR_RESENIA')")
    @Operation(summary = "Eliminar reseña por ID", description = "Elimina una reseña utilizando su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/eliminarReseniaaByID/{id}")
    public ResponseEntity<String> eliminarReseniaPorId(@PathVariable("id") Long id)
    {
        reseniaService.delete(id);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('ELIMINAR_RESENIA')")
    @Operation(summary = "Eliminar reseña por usuario y contenido", description = "Elimina una reseña según el ID del usuario y del contenido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/eliminarReseniaByUserAndIDContenido/{id_usuario}/{id_contenido}")
    public ResponseEntity<String> eliminarReseniaByUsuarioYContenido(@PathVariable("id_usuario") Long id_usuario,
                                            @PathVariable("id_contenido") Long id_contenido)
    {
        reseniaService.delete(id_usuario, id_contenido);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('VER_RESENIAS_POR_USUARIO')")
    @Operation(summary = "Listar reseñas por usuario", description = "Devuelve una lista paginada de reseñas realizadas por un usuario específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id_usuario}")
    public ResponseEntity<Page<ReseniaDTO>> listarPorUsuario(@PathVariable("id_usuario") Long id_usuario,
                                                             @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reseniaService.listarReseniasPorUsuario(id_usuario, pageable));
    }

    @PreAuthorize("hasAuthority('MODIFICAR_RESENIA')")
    @Operation(summary = "Modificar reseña", description = "Permite modificar una reseña ya existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña modificada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PatchMapping("/{id_usuario}/{id_contenido}")
    public ResponseEntity<String> modificar (@PathVariable("id_usuario") Long id_usuario,
                                        @PathVariable("id_contenido") Long id_contenido,
                                             @Valid @RequestBody ReseniaModificarDTO dto)
    {
        reseniaService.modificarResenia(id_usuario, id_contenido, dto);

        return ResponseEntity.ok("Reseña modificada correctamente");
    }


}
