package com.example.demo.model.controllers.user;

import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.ListasSinContDTO;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import com.example.demo.model.services.Usuarios.ListasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/listas")
public class ListasController {
    private final ListasService listasService;

    public ListasController(ListasService listasService) {
        this.listasService = listasService;
    }

    // metodos de listas de contenidos--------------------------
    @PostMapping("/{id}/crearLista")
    public ResponseEntity<String> crearLista(@PathVariable("id") Long idUser,@Valid @RequestBody ListasSinContDTO lista){
        listasService.addLista(idUser, lista);
        return ResponseEntity.ok("Lista Creada!");
    }

    @PatchMapping("/{id}/agregarALista")
    public ResponseEntity<String> agregarALista(@PathVariable("id") Long id,@RequestParam String nombre)
    {
        listasService.agregarContenido(id, nombre);
        return ResponseEntity.ok("Lista Agregada!");
    }

    // mostrar listas con contenido y sin----------------------------------

    @GetMapping("/{id}/verListas")
    public ResponseEntity<Page<ListasSinContDTO>> verListas(@PathVariable("id") Long idUser, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(listasService.getListas(idUser, pageable));
    }

    @GetMapping("/{id}/verLista/{nombre}")
    public ResponseEntity<ListaContenidoDTO>verLista(@PathVariable("id") Long idUser, @PathVariable("nombre") String nombre){
        return ResponseEntity.ok(listasService.verListaXnombre(idUser, nombre));
    }

    //-------------------- editar variables de listas ------------
    @PatchMapping("/{id}/{nombre}/cambiarNombre")
    public ResponseEntity<String> cambiarNombre(@PathVariable("id") Long id,@PathVariable("nombre") String nombre, @RequestParam String newNombre){
        listasService.cambiarNombre(id, nombre, newNombre);
        return ResponseEntity.ok("Nombre cambiado!");
    }

    @PatchMapping("/{id}/{nombre}/cambiarEstado")
    public ResponseEntity<String> cambiarEstado(@PathVariable("id") Long id,@PathVariable("nombre") String nombre, @RequestParam boolean newEstado){
        listasService.cambiarPrivado(id, nombre, newEstado);
        return ResponseEntity.ok("Privacidad cambiada!");
    }


    // ------------------------ borrar contenido o borrar lista completa------------
    @DeleteMapping("/{id}/sacarDelista")
    public ResponseEntity<String> eliminarDeLista(@PathVariable("id") Long id, @RequestParam String nombre){
        listasService.eliminarContenido(id, nombre);
        return ResponseEntity.ok("Contenido eliminado!");
    }

    @DeleteMapping("/{id}/eliminarLista")
    public ResponseEntity<String> eliminarLista(@PathVariable("id") Long idUser, @RequestParam String nombre){
        listasService.eliminarLista(idUser, nombre);
        return ResponseEntity.ok("Lista Eliminada!");
    }

    //buscar lista de otro usuario
    @GetMapping("/{username}/verListasDeUser")
    public ResponseEntity<Page<ListaContenidoDTO>> verListas(@PathVariable("username") String username, Pageable pageable){
        return ResponseEntity.ok(listasService.verListaDeUser(username, pageable));
    }




}
