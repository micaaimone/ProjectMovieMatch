package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.PeliculaDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.exceptions.ContenidoNotFound;
import com.example.demo.model.exceptions.PeliculaNotFound;
import com.example.demo.model.mappers.Contenido.PeliculaMapper;
import com.example.demo.model.repositories.Contenido.PeliculaRepository;
import com.example.demo.model.services.IContenido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PeliculaService implements IContenido<PeliculaDTO>{
    private final PeliculaRepository peliculaRepository;
    private final PeliculaMapper peliculaMapper;


    public PeliculaService(PeliculaRepository peliculaRepository, PeliculaMapper peliculaMapper) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
    }

    public Page<PeliculaDTO> datosBDD(Pageable pageable)
    {

        return peliculaRepository.findByEstado(0, pageable)
                .map(peliculaMapper::convertToDTO);
    }

    @Override
    public PeliculaDTO buscarByID(Long id) {
        return peliculaRepository.findById(id).map(peliculaMapper::convertToDTO)
                .orElseThrow(() -> new PeliculaNotFound("No existe una pelicula con ese ID"));    }

}
