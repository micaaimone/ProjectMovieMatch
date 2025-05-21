package com.example.demo.model.services;

import com.example.demo.model.DTOs.SerieDTO;
import com.example.demo.model.mappers.SerieMapper;
import com.example.demo.model.repositories.SerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SerieService {
    private final SerieRepository serieRepository;
    private final SerieMapper serieMapper;

    public SerieService(SerieRepository serieRepository, SerieMapper serieMapper) {
        this.serieRepository = serieRepository;
        this.serieMapper = serieMapper;
    }

    public Page<SerieDTO> datosSerieBDD(Pageable pageable)
    {
        return  serieRepository.findAll(pageable)
                .map(serieMapper::convertToDTO);
    }
}
