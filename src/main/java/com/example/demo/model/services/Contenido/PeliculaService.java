package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.Specifications.ContenidoSpecification;
import com.example.demo.model.Specifications.PeliculaSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.exceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.PeliculaNotFound;
import com.example.demo.model.mappers.Contenido.PeliculaMapper;
import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.services.IContenido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final PeliculaMapper peliculaMapper;


    public PeliculaService(PeliculaRepository peliculaRepository, PeliculaMapper peliculaMapper) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
    }

    public Page<PeliculaDTO> buscar(Pageable pageable,String genero, String anio, String titulo,Double puntuacion, Integer estado, String clasificacion, String metascore){
        Specification<PeliculaEntity> specification = Specification
                .where(PeliculaSpecification.genero(genero))
                .and(PeliculaSpecification.anio(anio))
                .and(PeliculaSpecification.tituloParecido(titulo))
                .and(PeliculaSpecification.puntuacion(puntuacion))
                .and(PeliculaSpecification.estado(estado))
                .and(PeliculaSpecification.clasificacion(clasificacion))
                .and(PeliculaSpecification.metascore(metascore));

        return peliculaRepository.findAll(specification, pageable).map(peliculaMapper::convertToDTO);
    }

}
