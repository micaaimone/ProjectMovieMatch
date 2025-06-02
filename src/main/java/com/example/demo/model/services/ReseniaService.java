package com.example.demo.model.services;

import com.example.demo.model.DTOs.ReseniaDTO;
import com.example.demo.model.DTOs.ReseniaModificarDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.ReseniaEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaAlredyExists;
import com.example.demo.model.exceptions.ContenidoExceptions.ReseniaNotFound;
import com.example.demo.model.mappers.Contenido.ReseniaMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.repositories.Contenido.ReseniaRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContenidoRepository contenidoRepository;
    private final ReseniaMapper reseniaMapper;

    @Autowired
    public ReseniaService(ReseniaRepository reseniaRepository, UsuarioRepository usuarioRepository, ContenidoRepository contenidoRepository, ReseniaMapper reseniaMapper) {
        this.reseniaRepository = reseniaRepository;
        this.usuarioRepository = usuarioRepository;
        this.contenidoRepository = contenidoRepository;
        this.reseniaMapper = reseniaMapper;
    }

    public void save(ReseniaDTO dto)
    {
        UsuarioEntity usuario = existeUsuario(dto);

        ContenidoEntity contenido = existeContenido(dto);

        if (reseniaRepository.findByIDAndContenido(dto.getId_usuario(), dto.getId_contenido()).isEmpty())
        {
            ReseniaEntity resenia = reseniaMapper.convertToEntity(dto, usuario, contenido);

            reseniaRepository.save(resenia);
        } else
        {
            throw new ReseniaAlredyExists("ya hay una reseña creada por este usuario en este contenido");
        }

    }

    public UsuarioEntity existeUsuario(ReseniaDTO dto)
    {

        return usuarioRepository.findById(dto.getId_usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public ContenidoEntity existeContenido(ReseniaDTO dto)
    {
        return contenidoRepository.findById(dto.getId_contenido())
            .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
    }



    //NO BORRA DE LA BDD, NO ENTIENDO

    public void delete(Long id) {
        boolean resenia = reseniaRepository.existsById(id);

        if (resenia)
        {
            reseniaRepository.deleteById(id);
        } else
        {
            throw new ReseniaNotFound("No existe una reseña con ese id");
        }
    }

    @Transactional
    public void delete(Long id_u, Long id_c)
    {
        ReseniaEntity resenia = reseniaRepository.findByIDAndContenido(id_u, id_c)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrada"));

        System.out.println("ID reseña: " + resenia.getId_resenia());
        System.out.println("Puntuación: " + resenia.getPuntuacionU());
        System.out.println("Comentario: " + resenia.getComentario());
        System.out.println("Fecha: " + resenia.getFecha());

        System.out.println("ID usuario: " + resenia.getUsuario().getId());
        System.out.println("Nombre usuario: " + resenia.getUsuario().getNombre());

        System.out.println("ID contenido: " + resenia.getContenido().getId_contenido());
        System.out.println("Título contenido: " + resenia.getContenido().getTitulo());

        UsuarioEntity usuario = resenia.getUsuario();
        usuario.getReseñasHechas().remove(resenia);

        ContenidoEntity contenido = resenia.getContenido();
        contenido.getReseña().remove(resenia);

        //preguntar xq tengo q borrar manualmente las reseñas de las listas de contenido y usuario
        //si tengo cascade.all en ambas entities
        reseniaRepository.delete(resenia);
    }

    public Page<ReseniaDTO> listarReseniasPorUsuario(Long id_usuario, Pageable pageable)
    {
        Page<ReseniaEntity> page = reseniaRepository.findAllById(id_usuario, pageable);


        if (page.getContent().isEmpty()) {
            throw new ReseniaNotFound("No se encontraron reseñas para este usuario.");
        }

        return page.map(reseniaMapper::convertToDTO);
    }

    public void modificarResenia(Long id_usuario, Long id_contenido, ReseniaModificarDTO dto) {
        if (dto.getComentario() == null && dto.getPuntuacionU() == null) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar");
        }

        ReseniaEntity resenia = reseniaRepository.findByIDAndContenido(id_usuario, id_contenido)
                .orElseThrow(() -> new ReseniaNotFound("Reseña no encontrada"));

        if (dto.getPuntuacionU() != null) {
            resenia.setPuntuacionU(dto.getPuntuacionU());
        }
        if (dto.getComentario() != null) {
            resenia.setComentario(dto.getComentario());
        }

        reseniaRepository.save(resenia);
    }
}
