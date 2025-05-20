package com.example.demo.model.controllers;

import com.example.demo.model.entities.ContenidoEntity;
import com.example.demo.model.entities.PeliculaEntity;
import com.example.demo.model.entities.SerieEntity;
import com.example.demo.model.services.APIMovieService;
import com.example.demo.model.services.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ContenidoController {

    private final ContenidoService contenidoService;

    @Autowired
    public ContenidoController( ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }


    @GetMapping("/verSeries")
    public Page<SerieEntity> listarSeries(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosSerieBDD(pageable);
    }

    @GetMapping("/verPeliculas")
    public Page<PeliculaEntity> listarPeliculas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosPeliculaBDD(pageable);
    }

    @GetMapping("/verSeriesYPeliculas")
    public Page<ContenidoEntity> ListarPeliculasYSeries(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return contenidoService.datosBDD(pageable);
    }

}