package com.example.demo.model.services;

import com.example.demo.model.DTOs.Contenido.ContenidoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IContenido<T> {
    public T buscarByID(Long id);
    public Page<T> datosBDD(Pageable pageable);

}
