package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.CredencialEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.entities.subs.ListasContenidoEntity;
import com.example.demo.model.exceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoYaAgregado;
import com.example.demo.model.exceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.user.ListasMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.ListasContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListasService {
    private final ListasContenidoRepository listasContenidoRepository;
    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ListasMapper listasMapper;

    public ListasService(ListasContenidoRepository listasContenidoRepository, ContenidoRepository contenidoRepository, UsuarioRepository usuarioRepository, ListasMapper listasMapper) {
        this.listasContenidoRepository = listasContenidoRepository;
        this.contenidoRepository = contenidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.listasMapper = listasMapper;
    }

    //crear una lista(con contenido vacio)
    public ResponseEntity<ListasContenidoEntity> addLista(Long idUser, String nombreLista) {
        ListasContenidoEntity lista = new ListasContenidoEntity();
        UsuarioEntity usuario = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException(idUser));

        lista.setUsuario(usuario);
        lista.setNombre(nombreLista);

        return ResponseEntity.status(HttpStatus.CREATED).body(listasContenidoRepository.save(lista));
    }

    //agregamos contenido a una lista existente(chequear)
    public ResponseEntity<ListasContenidoEntity> agregarContenido(Long idLista, String nombre) {
        ListasContenidoEntity lista = listasContenidoRepository.findById(idLista)
                .orElseThrow();

        Specification<ContenidoEntity> specification = Specification
                .where(ContenidoSpecification.tituloParecido(nombre));

        ContenidoEntity contenido = contenidoRepository.findAll(specification)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ContenidoNotFound("Contenido no encontrado"));

        if (!lista.getContenidos().contains(contenido)) {
            lista.getContenidos().add(contenido);
        }else{
            throw new ContenidoYaAgregado("Contenido existente en la lista");
        }

        return ResponseEntity.status(HttpStatus.OK).body(listasContenidoRepository.save(lista));
    }


    //crear dto sin lista de contenidos solo para visualizar las peliculas
    //muestra solo las listas creadas, sin los contenidos
    public Page<ListaContenidoDTO> getListas(Long idUser, Pageable pageable) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException(idUser));


        return listasContenidoRepository.findByIdUser(idUser, pageable)
                .map(listasMapper::convertToDTO);
    }

    //Muestra el contenido de una lista ya existente
    public ListaContenidoDTO verListaXnombre(Long idUser, String nombre, Pageable pageable) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException(idUser));

        return listasContenidoRepository.findByNombre(idUser, nombre)
                .map(listasMapper::convertToDTO)
                .orElseThrow(() -> new UsuarioNoEncontradoException(idUser));
    }

    //elimina un contenido de una lista existente
    public ResponseEntity<ListasContenidoEntity> eliminarContenido(Long idLista, String nombre) {
        ListasContenidoEntity lista = listasContenidoRepository.findById(idLista).orElseThrow();

        boolean removed = lista.getContenidos().removeIf(c -> c.getTitulo().equalsIgnoreCase(nombre));

        if (!removed) {
            throw new ContenidoNotFound(nombre);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listasContenidoRepository.save(lista));
    }



}
