package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.ListasSinContDTO;
import com.example.demo.model.services.Usuarios.ListasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/listas")
public class ListasController {
    private final ListasService listasService;

    public ListasController(ListasService listasService) {
        this.listasService = listasService;
    }

    // metodos de listas de contenidos--------------------------
    @PreAuthorize("hasAuthority('LISTA_CREAR')")
    @PostMapping("/{id}/crearLista")
    public ResponseEntity<String> crearLista(@PathVariable("id") Long idUser,@Valid @RequestBody ListasSinContDTO lista){
        listasService.addLista(idUser, lista);
        return ResponseEntity.ok("Lista Creada!");
    }

    @PreAuthorize("hasAuthority('LISTA_AGREGAR_CONTENIDO')")
    @PatchMapping("/{id}/agregarALista")
    public ResponseEntity<String> agregarALista(@PathVariable("id") Long id,@RequestParam String nombre)
    {
        listasService.agregarContenido(id, nombre);
        return ResponseEntity.ok("Lista Agregada!");
    }

    // mostrar listas con contenido y sin----------------------------------
    @PreAuthorize("hasAuthority('LISTA_VER_PROPIAS')")
    @GetMapping("/{id}/verListas")
    public ResponseEntity<Page<ListasSinContDTO>> verListas(@PathVariable("id") Long idUser, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(listasService.getListas(idUser, pageable));
    }

    // ------------------ Ver una lista específica
    @PreAuthorize("hasAuthority('LISTA_VER_DETALLE')")
    @GetMapping("/{id}/verLista/{nombre}")
    public ResponseEntity<ListaContenidoDTO>verLista(@PathVariable("id") Long idUser, @PathVariable("nombre") String nombre){
        return ResponseEntity.ok(listasService.verListaXnombre(idUser, nombre));
    }

    //-------------------- editar variables de listas ------------
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_NOMBRE')")
    @PatchMapping("/{id}/{nombre}/cambiarNombre")
    public ResponseEntity<String> cambiarNombre(@PathVariable("id") Long id,@PathVariable("nombre") String nombre, @RequestParam String newNombre){
        listasService.cambiarNombre(id, nombre, newNombre);
        return ResponseEntity.ok("Nombre cambiado!");
    }

    // ------------------ Cambiar estado de privacidad
    @PreAuthorize("hasAuthority('LISTA_CAMBIAR_ESTADO')")
    @PatchMapping("/{id}/{nombre}/cambiarEstado")
    public ResponseEntity<String> cambiarEstado(@PathVariable("id") Long id,@PathVariable("nombre") String nombre, @RequestParam boolean newEstado){
        listasService.cambiarPrivado(id, nombre, newEstado);
        return ResponseEntity.ok("Privacidad cambiada!");
    }


    // ------------------------ borrar contenido o borrar lista completa------------
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR_CONTENIDO')")
    @DeleteMapping("/{id}/sacarDelista")
    public ResponseEntity<String> eliminarDeLista(@PathVariable("id") Long id, @RequestParam String nombre){
        listasService.eliminarContenido(id, nombre);
        return ResponseEntity.ok("Contenido eliminado!");
    }

    // ------------------ Eliminar lista completa
    @PreAuthorize("hasAuthority('LISTA_ELIMINAR')")
    @DeleteMapping("/{id}/eliminarLista")
    public ResponseEntity<String> eliminarLista(@PathVariable("id") Long idUser, @RequestParam String nombre){
        listasService.eliminarLista(idUser, nombre);
        return ResponseEntity.ok("Lista Eliminada!");
    }

    // ------------------ Ver listas de otro usuario
    @PreAuthorize("hasAuthority('LISTA_VER_PUBLICAS')")
    @GetMapping("/{username}/verListasDeUser")
    public ResponseEntity<Page<ListaContenidoDTO>> verListas(@PathVariable("username") String username, Pageable pageable){
        return ResponseEntity.ok(listasService.verListaDeUser(username, pageable));
    }




}
