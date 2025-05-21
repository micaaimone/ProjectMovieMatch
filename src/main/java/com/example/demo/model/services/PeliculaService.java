package com.example.demo.model.services;

import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.mappers.PeliculaMapper;
import com.example.demo.model.repositories.PeliculaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final PeliculaMapper peliculaMapper;


    public PeliculaService(PeliculaRepository peliculaRepository, PeliculaMapper peliculaMapper) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
    }

    public Page<PeliculaDTO> datosPeliculaBDD(Pageable pageable)
    {
        return peliculaRepository.findAll(pageable)
                .map(peliculaMapper::convertToDTO);
    }
}
