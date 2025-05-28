package com.example.demo.model.Specifications.Contenido;

import com.example.demo.model.entities.Contenido.SerieEntity;
import org.springframework.data.jpa.domain.Specification;

public class SerieSpecification {

    public static Specification<SerieEntity> genero(String genero)
    {
        return ((root, query, criteriaBuilder) ->
                genero == null ? null :
                        criteriaBuilder.equal(root.get("genero"), genero)
        );
    }

    public static Specification<SerieEntity> estado(Integer estado)
    {
        return ((root, query, criteriaBuilder) ->
                estado == null ? null :
                        criteriaBuilder.equal(root.get("estado"), estado)
        );
    }

    public static Specification<SerieEntity> anio(String anio)
    {
        return ((root, query, criteriaBuilder) ->
                anio == null ? null :
                        criteriaBuilder.equal(root.get("anio"), anio)
        );
    }

    public static Specification<SerieEntity> tituloParecido(String titulo)
    {
        return ((root, query, criteriaBuilder) ->
                titulo == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("titulo")),
                                "%" + titulo.toLowerCase() + "%"
                        ));
    }

    public static Specification<SerieEntity> puntuacion(Double puntuacion)
    {
        return ((root, query, criteriaBuilder) ->
                puntuacion == null ? null :
                        criteriaBuilder.equal(root.get("puntuacion"), puntuacion)
        );
    }

    public static Specification<SerieEntity> clasificacion(String clasificacion)
    {
        return ((root, query, criteriaBuilder) ->
                clasificacion == null ? null :
                        criteriaBuilder.equal(root.get("clasificacion"), clasificacion)
        );
    }

    public static Specification<SerieEntity> temporadas(String temporadas)
    {
        return ((root, query, criteriaBuilder) ->
                temporadas == null ? null :
                        criteriaBuilder.equal(root.get("temporadas"), temporadas)
        );
    }
}
