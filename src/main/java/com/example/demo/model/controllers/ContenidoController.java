package com.example.demo.model.controllers;

import com.example.demo.model.DTOs.ContenidoDTO;
import com.example.demo.model.DTOs.PeliculaDTO;
import com.example.demo.model.DTOs.SerieDTO;
import com.example.demo.model.services.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController( ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }


    @GetMapping("/verSeries")
    public Page<SerieDTO> listarSeries(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosSerieBDD(pageable);
    }

    @GetMapping("/verPeliculas")
    public Page<PeliculaDTO> listarPeliculas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosPeliculaBDD(pageable);
    }

    @GetMapping("/verSeriesYPeliculas")
    public Page<ContenidoDTO> ListarPeliculasYSeries(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosBDD(pageable);
    }

}