package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.Contenido.ContenidoMostrarDTO;
import com.example.demo.model.DTOs.user.Grupo.ModificarGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.NewGrupoDTO;
import com.example.demo.model.DTOs.user.Grupo.VisualizarGrupoDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.services.Usuarios.GrupoService;
import com.example.demo.model.services.Usuarios.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios/grupo")
public class GrupoController {
    private final GrupoService grupoService;
    private final UsuarioService usuarioService;

    @Autowired
    public GrupoController(GrupoService grupoService, UsuarioService usuarioService) {
        this.grupoService = grupoService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Crear grupo",
            description = "Permite crear un nuevo grupo de usuarios"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    @PreAuthorize("hasAuthority('CREAR_GRUPO')")
    @PostMapping
    public ResponseEntity<String> crearGrupo(@Valid @RequestBody NewGrupoDTO newGrupoDTO) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        grupoService.save(newGrupoDTO, usuarioAutenticado.getId());
        return ResponseEntity.ok("Grupo creado correctamente");
    }

    @Operation(
            summary = "Visualizar grupo por ID",
            description = "Permite visualizar un grupo si el usuario pertenece a él"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('VER_GRUPO')")
    @GetMapping("/{idGrupo}")
    public ResponseEntity<VisualizarGrupoDTO> visualizarGrupo(@PathVariable Long idGrupo) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        VisualizarGrupoDTO grupo = grupoService.mostrarGrupoPorID(idGrupo, usuarioAutenticado.getId());
        return ResponseEntity.ok(grupo);
    }

    @Operation(
            summary = "Visualizar grupos del usuario autenticado",
            description = "Devuelve una lista paginada de los grupos a los que pertenece el usuario autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupos obtenidos correctamente")
    })
    @PreAuthorize("hasAuthority('VER_GRUPO')")
    @GetMapping
    public ResponseEntity<Page<VisualizarGrupoDTO>> visualizarGruposDelUsuario(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(grupoService.visualizarGrupoPorUsuario(usuarioAutenticado.getId(), page, size));
    }

    @Operation(
            summary = "Visualizar grupo por nombre",
            description = "Permite buscar un grupo por nombre si el usuario pertenece a él"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('VER_GRUPO')")
    @GetMapping("/buscar")
    public ResponseEntity<VisualizarGrupoDTO> visualizarGrupoPorNombre(@RequestParam String nombre) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        return ResponseEntity.ok(grupoService.visualizarPorNombre(nombre, usuarioAutenticado.getId()));
    }

    @Operation(
            summary = "Modificar grupo",
            description = "Permite modificar nombre y/o descripción de un grupo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo modificado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('MODIFICAR_GRUPO')")
    @PatchMapping("/{idGrupo}")
    public ResponseEntity<String> modificarGrupo(
            @PathVariable Long idGrupo,
            @Valid @RequestBody ModificarGrupoDTO dto) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        grupoService.modificarGrupo(usuarioAutenticado.getId(), idGrupo, dto);
        return ResponseEntity.ok("Grupo modificado correctamente");
    }

    @Operation(
            summary = "Agregar usuario a grupo",
            description = "Permite al administrador de un grupo agregar un usuario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario agregado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('AGREGAR_USUARIO_A_GRUPO')")
    @PostMapping("/{idGrupo}/usuarios/{idUsuario}")
    public ResponseEntity<String> agregarUsuarioAGrupo(
            @PathVariable Long idGrupo,
            @PathVariable Long idUsuario) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        grupoService.agregarUsuarioAGrupo(idGrupo, usuarioAutenticado.getId(), idUsuario);
        return ResponseEntity.ok("Usuario agregado al grupo correctamente");
    }

    @Operation(
            summary = "Eliminar usuario de grupo",
            description = "Permite al administrador de un grupo eliminar un usuario"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('ELIMINAR_USUARIO_DE_GRUPO')")
    @DeleteMapping("/{idGrupo}/usuarios/{idUsuario}")
    public ResponseEntity<String> eliminarUsuarioDeGrupo(
            @PathVariable Long idGrupo,
            @PathVariable Long idUsuario) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        grupoService.eliminarUsuarioDeGrupo(idGrupo, usuarioAutenticado.getId(), idUsuario);
        return ResponseEntity.ok("Usuario eliminado del grupo correctamente");
    }

    @Operation(
            summary = "Eliminar grupo",
            description = "Permite al administrador eliminar completamente un grupo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('ELIMINAR_GRUPO')")
    @DeleteMapping("/{idGrupo}")
    public ResponseEntity<String> eliminarGrupo(@PathVariable Long idGrupo) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();
        grupoService.eliminarGrupo(usuarioAutenticado.getId(), idGrupo);
        return ResponseEntity.ok("Grupo eliminado correctamente");
    }

    @Operation(
            summary = "Ver coincidencias",
            description = "Permite ver las coincidencias del grupo según el id del grupo con paginación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de coincidencias obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o usuario no encontrado")
    })
    @PreAuthorize("hasAuthority('VER_GRUPO')")
    @GetMapping("/coincidencias/{idGrupo}")
    public ResponseEntity<Page<ContenidoMostrarDTO>>coincidencias(@PathVariable Long idGrupo,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        UsuarioEntity usuarioAutenticado = usuarioService.getUsuarioAutenticado();

        Page<ContenidoMostrarDTO> resultado = grupoService.mostrarCoincidencias(idGrupo, page, size);

        return ResponseEntity.ok(resultado);
    }

}
