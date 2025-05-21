package com.example.demo.model.services;

import com.example.demo.model.DTOs.ContenidoDTO;
import com.example.demo.model.mappers.ContenidoMapper;
import com.example.demo.model.repositories.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public Page<ContenidoDTO> datosBDD(Pageable pageable)
    {
        return contenidoRepository.findAll(pageable)
                .map(contenidoMapper::convertToDTO);
    }

}
