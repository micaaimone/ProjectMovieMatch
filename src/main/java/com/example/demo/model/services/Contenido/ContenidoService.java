package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.Specifications.Contenido.ContenidoSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final ContenidoMapper contenidoMapper;


    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository,ContenidoMapper contenidoMapper) {
        this.contenidoRepository = contenidoRepository;
        this.contenidoMapper = contenidoMapper;
    }



    public Page<ContenidoDTO> buscar(Pageable pageable,String genero, String anio, String titulo,Double puntuacion, Integer estado, String clasificacion){
        Specification<ContenidoEntity> specification = Specification
                .where(ContenidoSpecification.genero(genero))
                .and(ContenidoSpecification.anio(anio))
                .and(ContenidoSpecification.tituloParecido(titulo))
                .and(ContenidoSpecification.puntuacion(puntuacion))
                .and(ContenidoSpecification.estado(estado))
                .and(ContenidoSpecification.clasificacion(clasificacion));

        return contenidoRepository.findAll(specification, pageable).map(contenidoMapper::convertToDTO);
    }


    public boolean borrarDeBDD(long id)
    {
        boolean eliminado;
        if(contenidoRepository.existsById(id))
        {
            Optional<ContenidoEntity> c = contenidoRepository.findById(id);
            if(c.isPresent())
            {
                ContenidoEntity contenido = c.get();
                contenido.setEstado(1);
                contenidoRepository.save(contenido);
            }
            eliminado = true;
        } else
        {
            eliminado = false;
        }
        return eliminado;
    }



    public boolean darDeAltaBDD(long id)
    {
        boolean alta;
        if(contenidoRepository.existsById(id))
        {
            Optional<ContenidoEntity> c = contenidoRepository.findById(id);
            if(c.isPresent())
            {
                ContenidoEntity contenido = c.get();
                contenido.setEstado(0);
                contenidoRepository.save(contenido);
            }
            alta = true;
        } else
        {
            alta = false;
        }
        return alta;
    }


}
