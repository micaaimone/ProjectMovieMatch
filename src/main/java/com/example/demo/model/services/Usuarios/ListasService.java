package com.example.demo.model.services.Usuarios;

import com.example.demo.model.DTOs.user.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.ListasSinContDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.User.ListasContenidoEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoYaAgregadoException;
import com.example.demo.model.exceptions.UsuarioExceptions.ListAlreadyExistsException;
import com.example.demo.model.exceptions.UsuarioExceptions.ListaNotFoundException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
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
    public void addLista(Long idUser, ListasSinContDTO list) {
        UsuarioEntity usuario = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if(listasContenidoRepository.findByNombre(idUser, list.getNombre()).isPresent()){
            throw new ListAlreadyExistsException("Lista ya existe");
        }

        ListasContenidoEntity lista = listasMapper.convertToEntitySC(list);
        lista.setUsuario(usuario);

        listasContenidoRepository.save(lista);
    }

    //agregamos contenido a una lista existente(chequear)
    public void agregarContenido(Long idLista, String nombre) {
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
            throw new ContenidoYaAgregadoException("Contenido existente en la lista");
        }

        listasContenidoRepository.save(lista);
    }


    //muestra solo las listas creadas, sin los contenidos
    public Page<ListasSinContDTO> getListas(Long idUser, Pageable pageable) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));


        return listasContenidoRepository.findByIdUser(idUser, pageable)
                .map(listasMapper::convertToDTOSC);
    }

    //Muestra el contenido de una lista ya existente
    public ListaContenidoDTO verListaXnombre(Long idUser, String nombre) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        return listasContenidoRepository.findByNombre(idUser, nombre)
                .map(listasMapper::convertToDTO)
                .orElseThrow(() -> new ListaNotFoundException("Lista no encontrada"));
    }

    //ver lista de otro usuario si es publica
    public Page<ListaContenidoDTO> verListaDeUser(String username, Pageable pageable) {
        return listasContenidoRepository.listaOtroUser(username, pageable)
                .map(listasMapper::convertToDTO);
    }

    //elimina un contenido de una lista existente
    public void eliminarContenido(Long idLista, String nombre) {
        ListasContenidoEntity lista = listasContenidoRepository.findById(idLista).orElseThrow();

        boolean removed = lista.getContenidos().removeIf(c -> c.getTitulo().equalsIgnoreCase(nombre));

        if (!removed) {
            throw new ContenidoNotFound(nombre);
        }

        listasContenidoRepository.save(lista);
    }

    //cambiar nombre de lista
    public void cambiarNombre(Long idUser, String nombre, String newnombre) {

        if(newnombre != null) {

            ListasContenidoEntity lista = listasContenidoRepository.findByNombre(idUser, nombre)
                            .orElseThrow(() -> new ListaNotFoundException("Lista no encontrada"));

            lista.setNombre(newnombre);
            listasContenidoRepository.save(lista);
        }
        else{
            throw new IllegalArgumentException("nombre invalido");
        }
    }

    public void cambiarPrivado(Long idUsuario, String nombre, boolean privado) {
        ListasContenidoEntity lista = listasContenidoRepository.findByNombre(idUsuario, nombre)
                        .orElseThrow(()-> new ListaNotFoundException("Lista no encontrada"));

        lista.setPrivado(privado);
        listasContenidoRepository.save(lista);
    }

    public void eliminarLista(Long idUser, String nombreLista){
        ListasContenidoEntity lista = listasContenidoRepository.findByNombre(idUser, nombreLista)
                .orElseThrow(() -> new ListaNotFoundException("Lista no encontrada"));

        //eliminamos de la bdd la tabla intermedia
        lista.getContenidos().clear();
        listasContenidoRepository.save(lista);

        listasContenidoRepository.delete(lista);
    }

}
