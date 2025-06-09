package com.example.demo.model.Specifications.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import org.springframework.data.jpa.domain.Specification;


public class ContenidoSpecification {

    public static Specification<ContenidoEntity> genero(String genero)
    {
        return ((root, query, criteriaBuilder) ->
                genero == null ? null :
                        criteriaBuilder.equal(root.get("genero"), genero)
        );
    }

    public static Specification<ContenidoEntity> id(Long id)
    {
        return ((root, query, criteriaBuilder) ->
                id == null ? null :
                        criteriaBuilder.equal(root.get("id"), id)
        );
    }

    public static Specification<ContenidoEntity> activo(Boolean activo) {
        return (root, query, cb) ->
                activo == null ? null :
                        cb.equal(root.get("activo"), activo);
    }


    public static Specification<ContenidoEntity> anio(String anio)
    {
        return ((root, query, criteriaBuilder) ->
                anio == null ? null :
                        criteriaBuilder.equal(root.get("anio"), anio)
        );
    }

    public static Specification<ContenidoEntity> tituloParecido(String titulo)
    {
        return ((root, query, criteriaBuilder) ->
                titulo == null ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("titulo")),
                                "%" + titulo.toLowerCase() + "%"
                        ));
    }

    public static Specification<ContenidoEntity> puntuacion(Double puntuacion)
    {
        return ((root, query, criteriaBuilder) ->
                puntuacion == null ? null :
                        criteriaBuilder.equal(root.get("puntuacion"), puntuacion)
        );
    }

    public static Specification<ContenidoEntity> clasificacion(String clasificacion)
    {
        return ((root, query, criteriaBuilder) ->
                clasificacion == null ? null :
                        criteriaBuilder.equal(root.get("clasificacion"), clasificacion)
        );
    }


}
