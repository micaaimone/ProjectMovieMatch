package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.exceptions.ContenidoNotFound;
import com.example.demo.model.mappers.Contenido.ContenidoMapper;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import com.example.demo.model.services.IContenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ContenidoService implements IContenido<ContenidoDTO> {

    private final ContenidoRepository contenidoRepository;
    private final ContenidoMapper contenidoMapper;

    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository,ContenidoMapper contenidoMapper) {
        this.contenidoRepository = contenidoRepository;
        this.contenidoMapper = contenidoMapper;
    }


    public Page<ContenidoDTO> datosBDD(Pageable pageable)
    {
        return contenidoRepository.findByEstado(0,pageable)
                .map(contenidoMapper::convertToDTO);
    }

    public Page<ContenidoDTO> datosDadosDeBajaBDD(Pageable pageable)
    {
        return contenidoRepository.findByEstado(1,pageable)
                .map(contenidoMapper::convertToDTO);
    }

    public ContenidoDTO buscarByID(Long id)
    {
        return contenidoRepository.findById(id).map(contenidoMapper::convertToDTO)
                .orElseThrow(() -> new ContenidoNotFound("No existe un contenido con ese ID"));
    }

    public Page<ContenidoDTO> buscarPorGenero(String genero, Pageable pageable) {
        return contenidoRepository.findByGeneroAndEstado(genero, 0, pageable)
                .map(contenidoMapper::convertToDTO);
    }

    public Page<ContenidoDTO> filtrarPorAnio(int anio, Pageable pageable) {
        return contenidoRepository.findByAnioAndEstado(anio, 0, pageable)
                .map(contenidoMapper::convertToDTO);
    }

    public Page<ContenidoDTO> filtrarPorTituloParcial(String tituloParcial, Pageable pageable) {
        return contenidoRepository.findByTituloContainingIgnoreCaseAndEstado(tituloParcial, 0, pageable)
                .map(contenidoMapper::convertToDTO);
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
