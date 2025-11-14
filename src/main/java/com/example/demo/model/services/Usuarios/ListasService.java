package com.example.demo.model.services.Usuarios;

import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.RoleRepository;
import com.example.demo.model.DTOs.user.Listas.ListaContenidoDTO;
import com.example.demo.model.DTOs.user.Listas.ListasSinContDTO;
import com.example.demo.model.DTOs.user.Listas.ListaResumenDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.User.ListasContenidoEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoYaAgregadoException;
import com.example.demo.model.exceptions.ListasExceptions.ListAlreadyExistsException;
import com.example.demo.model.exceptions.ListasExceptions.ListaNotFoundException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.user.ListasMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Usuarios.ListasContenidoRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ListasService {
    private final ListasContenidoRepository listasContenidoRepository;
    private final ContenidoRepository contenidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ListasMapper listasMapper;
    private final RoleRepository roleRepository;

    public ListasService(ListasContenidoRepository listasContenidoRepository, ContenidoRepository contenidoRepository, UsuarioRepository usuarioRepository, ListasMapper listasMapper, RoleRepository roleRepository) {
        this.listasContenidoRepository = listasContenidoRepository;
        this.contenidoRepository = contenidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.listasMapper = listasMapper;
        this.roleRepository = roleRepository;
    }

    //crear una lista(con contenido vacio)
    public void addLista(Long idUser, ListasSinContDTO list) {
        UsuarioEntity usuario = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        RoleEntity rolPremium = roleRepository.findByRole(Role.ROLE_PREMIUM)
                .orElseThrow(() -> new RuntimeException("Rol PREMIUM no encontrado"));


        if(usuario.getCredencial().getRoles().contains(rolPremium) || 3 > usuario.getListas().size()) {

            if (listasContenidoRepository.findByNombre(idUser, list.getNombre()).isPresent()) {
                throw new ListAlreadyExistsException("Lista ya existe");
            }

            ListasContenidoEntity lista = listasMapper.convertToEntitySC(list);
            lista.setUsuario(usuario);

            listasContenidoRepository.save(lista);
        }else{
            throw new ListAlreadyExistsException("Ya cuenta con 3 listas, nuestro plan premium incluye listas ilimitadas!");
        }
    }

    //agregamos contenido a una lista existente
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
    public Page<ListaResumenDTO> getListas(Long idUser, Pageable pageable) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));


        return listasContenidoRepository.findByIdUser(idUser, pageable)
                .map(listasMapper::convertToDTOResumen);
    }

    //Muestra el contenido de una lista ya existente
    public ListaContenidoDTO verListaXnombre(Long idUser, String nombre) {
        UsuarioEntity user = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        return listasContenidoRepository.findByNombre(idUser, nombre)
                .map(listasMapper::convertToDTO)
                .orElseThrow(() -> new ListaNotFoundException("Lista no encontrada"));
    }

    //ver lista de otro usuario si es publica------------
    public Page<ListaContenidoDTO> verListaDeUser(String username, Pageable pageable) {
        return listasContenidoRepository.listaOtroUser(username, pageable)
                .map(listasMapper::convertToDTO);
    }

    //elimina un contenido de una lista existente ------------------
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

    //cambiar visibilidad de la lista
    public void cambiarPrivado(Long idUsuario, String nombre, boolean privado) {
        ListasContenidoEntity lista = listasContenidoRepository.findByNombre(idUsuario, nombre)
                        .orElseThrow(()-> new ListaNotFoundException("Lista no encontrada"));

        lista.setPrivado(privado);
        listasContenidoRepository.save(lista);
    }

    public void eliminarLista(Long idUser, String nombreLista){
        ListasContenidoEntity lista = listasContenidoRepository.findByNombre(idUser, nombreLista)
                .orElseThrow(() -> new ListaNotFoundException("Lista no encontrada"));

        // 1) Borrar filas de tabla intermedia
        listasContenidoRepository.eliminarAsociaciones(lista.getIdListaContenido());

        // 2) Limpiar la lista en memoria (importante!)
        lista.getContenidos().clear();
        listasContenidoRepository.save(lista);

        // 3) Eliminar la lista
        listasContenidoRepository.delete(lista);
    }

}
