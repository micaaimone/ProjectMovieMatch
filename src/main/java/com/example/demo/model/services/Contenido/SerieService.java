package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.SerieDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.exceptions.SerieNotFound;
import com.example.demo.model.mappers.Contenido.SerieMapper;
import com.example.demo.model.repositories.Contenido.SerieRepository;
import com.example.demo.model.services.IContenido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SerieService implements IContenido<SerieDTO>{
    private final SerieRepository serieRepository;
    private final SerieMapper serieMapper;

    public SerieService(SerieRepository serieRepository, SerieMapper serieMapper) {
        this.serieRepository = serieRepository;
        this.serieMapper = serieMapper;
    }

    @Override
    public SerieDTO buscarByID(Long id) {
        return serieRepository.findById(id).map(serieMapper::convertToDTO)
                .orElseThrow(() -> new SerieNotFound("No existe una serie con ese ID"));
    }

    public Page<SerieDTO> datosBDD(Pageable pageable) {
        return  serieRepository.findByEstado(0,pageable)
                .map(serieMapper::convertToDTO);    }
}
