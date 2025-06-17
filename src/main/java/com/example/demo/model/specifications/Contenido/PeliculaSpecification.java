package com.example.demo.model.Specifications.Contenido;

import com.example.demo.model.entities.Contenido.PeliculaEntity;
import org.springframework.data.jpa.domain.Specification;

public class PeliculaSpecification {

    public static Specification<PeliculaEntity> genero(String genero)
    {
        return ((root, query, criteriaBuilder) ->
                genero == null ? null :
                        criteriaBuilder.equal(root.get("genero"), genero)
        );
    }

    public static Specification<PeliculaEntity> id(Long id)
    {
        return ((root, query, criteriaBuilder) ->
                id == null ? null :
                        criteriaBuilder.equal(root.get("id"), id)
        );
    }

    public static Specification<PeliculaEntity> activo(Boolean activo) {
        return (root, query, cb) ->
                activo == null ? null :
                        cb.equal(root.get("activo"), activo);
    }


    public static Specification<PeliculaEntity> anio(String anio)
    {
        return ((root, query, criteriaBuilder) ->
                anio == null ? null :
                        criteriaBuilder.equal(root.get("anio"), anio)
        );
    }

    public static Specification<PeliculaEntity> tituloParecido(String titulo)
    {
        return ((root, query, criteriaBuilder) ->
                titulo == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("titulo")),
                                "%" + titulo.toLowerCase() + "%"
                        ));
    }

    public static Specification<PeliculaEntity> puntuacion(Double puntuacion)
    {
        return ((root, query, criteriaBuilder) ->
                puntuacion == null ? null :
                        criteriaBuilder.equal(root.get("puntuacion"), puntuacion)
        );
    }

    public static Specification<PeliculaEntity> clasificacion(String clasificacion)
    {
        return ((root, query, criteriaBuilder) ->
                clasificacion == null ? null :
                        criteriaBuilder.equal(root.get("clasificacion"), clasificacion)
        );
    }

    public static Specification<PeliculaEntity> metascore(String metascore)
    {
        return ((root, query, criteriaBuilder) ->
                metascore == null ? null :
                        criteriaBuilder.equal(root.get("metascore"), metascore)
        );
    }
}
