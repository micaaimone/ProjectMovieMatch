package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.exceptions.ContenidoExceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.ContenidoYaAgregadoException;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final ContenidoMapper contenidoMapper;


    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository,ContenidoMapper contenidoMapper) {
        this.contenidoRepository = contenidoRepository;
        this.contenidoMapper = contenidoMapper;
    }


    public Page<ContenidoDTO> buscarActivos(Pageable pageable,String genero, String anio, String titulo,Double puntuacion, Boolean estado, String clasificacion, Long id){
        Specification<ContenidoEntity> specification = Specification
                .where(ContenidoSpecification.genero(genero))
                .and(ContenidoSpecification.anio(anio))
                .and(ContenidoSpecification.tituloParecido(titulo))
                .and(ContenidoSpecification.puntuacion(puntuacion))
                .and(ContenidoSpecification.estado(estado))
                .and(ContenidoSpecification.clasificacion(clasificacion))
                .and(ContenidoSpecification.id(id));

        Page<ContenidoEntity> page = contenidoRepository.findAll(specification, pageable);


        if (page.getContent().isEmpty()) {
            throw new ContenidoNotFound("No se encontraron contenidos con los filtros especificados.");
        }

        return page.map(contenidoMapper::convertToDTO);
    }

    private ContenidoEntity obtenerContenidoPorId(long id) {
        return contenidoRepository.findById(id)
                .orElseThrow(() -> new ContenidoNotFound("No se encontró contenido con id: " + id));
    }



    public void darDeBajaContenido(long id) {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if(contenido.isActivo())
        {
            contenido.setActivo(false);
            contenidoRepository.save(contenido);
        } else {
            throw new ContenidoNotFound("No se encuentra un contenido con el id: " + id);
        }

    }



    public void darDeAltaContenido(long id)
    {
        ContenidoEntity contenido = obtenerContenidoPorId(id);
        if(contenido.isActivo()) {
            throw new ContenidoYaAgregadoException("El contenido ya fue agregado a la base de datos");
        } else {
            contenido.setActivo(true);
            contenidoRepository.save(contenido);
        }
    }


}
