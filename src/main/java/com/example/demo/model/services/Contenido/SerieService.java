package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.Specifications.ContenidoSpecification;
import com.example.demo.model.Specifications.SerieSpecification;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.exceptions.SerieNotFound;
import com.example.demo.model.mappers.Contenido.SerieMapper;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.services.IContenido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SerieService {
    private final SerieRepository serieRepository;
    private final SerieMapper serieMapper;

    public SerieService(SerieRepository serieRepository, SerieMapper serieMapper) {
        this.serieRepository = serieRepository;
        this.serieMapper = serieMapper;
    }

    public Page<SerieDTO> buscar(Pageable pageable,String genero, String anio, String titulo,Double puntuacion, Integer estado, String clasificacion, String temporadas){
        Specification<SerieEntity> specification = Specification
                .where(SerieSpecification.genero(genero))
                .and(SerieSpecification.anio(anio))
                .and(SerieSpecification.tituloParecido(titulo))
                .and(SerieSpecification.puntuacion(puntuacion))
                .and(SerieSpecification.estado(estado))
                .and(SerieSpecification.clasificacion(clasificacion))
                .and(SerieSpecification.temporadas(temporadas));

        return serieRepository.findAll(specification, pageable).map(serieMapper::convertToDTO);
    }

}

