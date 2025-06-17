package com.example.demo.model.services.Contenido;

import com.example.demo.model.DTOs.Contenido.ContenidoCompletoDTO;
import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.Contenido.PeliculaEntity;
import com.example.demo.model.entities.Contenido.SerieEntity;
import com.example.demo.model.repositories.Contenido.ContenidoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

@Service

public class APIMovieService {

    //usamos restTemplate para poder hacer peticiones a la API de pelis, es final porque solo lo quiero inicializar una vez y q no cambie
    private final RestTemplate restTemplate;
    //repo de bdd, ahi voy a guardar todas las cosas d la api, lo inyectamos por contructor y no queremos q se modifique dsps
    private final ContenidoRepository contenidoRepository;
    //url de la base de api, ahi hago las peticiones, lo dejo aca ya q lo uso varias veces, es final xq no cambia
    private final String apiUrl = "https://www.omdbapi.com/";
    //es la clave privada q me da la API para poder acceder a sus datos
    @Value("${API_KEY}")
    private String apiKey;

    public APIMovieService(RestTemplate restTemplate, ContenidoRepository contenidoRepository) {
        this.restTemplate = restTemplate;
        this.contenidoRepository = contenidoRepository;
    }


    public PeliculaEntity findPeliculaByTitle(String titulo) {
        String url = apiUrl + "?t=" + titulo + "&apikey=" + apiKey;
        return restTemplate.getForObject(url, PeliculaEntity.class);
    }

    public SerieEntity findSerieByTitle(String titulo) {
        String url = apiUrl + "?t=" + titulo + "&apikey=" + apiKey;
        return restTemplate.getForObject(url, SerieEntity.class);
    }

    public Optional<ContenidoEntity> findByAPIID(String id) {
        String url = apiUrl + "?i=" + id + "&apikey=" + apiKey;

        return Optional.ofNullable(restTemplate.getForObject(url, ContenidoEntity.class));
    }

    public ContenidoEntity save(ContenidoEntity dato) {
        return contenidoRepository.save(dato);
    }

    public void delete(ContenidoEntity dato) {
        contenidoRepository.delete(dato);
    }

    public ContenidoCompletoDTO findContenidoByTitle(String titulo) {
        String url = apiUrl + "?t=" + titulo + "&apikey=" + apiKey;
        return restTemplate.getForObject(url, ContenidoCompletoDTO.class);
    }

}

