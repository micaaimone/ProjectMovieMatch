package com.example.demo.model.controllers.Contenido;

import com.example.demo.model.DTOs.Resenia.ReseniaDTO;
import com.example.demo.model.DTOs.Resenia.ReseniaModificarDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Contenido.ReseniaService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios/resenia")
public class ReseniaController {
    //funcan
    private final ReseniaService reseniaService;
    private final UsuarioService usuarioService;

    @Autowired
    public ReseniaController(ReseniaService reseniaService, UsuarioService usuarioService) {
        this.reseniaService = reseniaService;
        this.usuarioService = usuarioService;
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
    @Operation(summary = "Eliminar reseña hecha por usuario, eliminando por id contenido", description = "Elimina una reseña según el ID del usuario y del contenido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/eliminarReseniaByUserAndIDContenido/{id_contenido}")
    public ResponseEntity<String> eliminarReseniaByUsuarioYContenido(@PathVariable("id_contenido") Long id_contenido)
    {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        reseniaService.delete(usuarioAutenticado.getId(), id_contenido);
        return ResponseEntity.ok("Reseña eliminada correctamente");
    }

    @PreAuthorize("hasAuthority('VER_RESENIAS_POR_USUARIO')")
    @Operation(summary = "Listar reseñas por usuario", description = "Devuelve una lista paginada de reseñas realizadas por un usuario específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping
    public ResponseEntity<Page<ReseniaDTO>> listarPorUsuario(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reseniaService.listarReseniasPorUsuario(usuarioAutenticado.getId(), pageable));
    }

    @PreAuthorize("hasAuthority('MODIFICAR_RESENIA')")
    @Operation(summary = "Modificar reseña", description = "Permite modificar una reseña ya existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña modificada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PatchMapping("/{id_contenido}")
    public ResponseEntity<String> modificar (@PathVariable("id_contenido") Long id_contenido,
                                             @Valid @RequestBody ReseniaModificarDTO dto)
    {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        reseniaService.modificarResenia(usuarioAutenticado.getId(), id_contenido, dto);

        return ResponseEntity.ok("Reseña modificada correctamente");
    }

    //--------------------metodos solo para admin
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


}
