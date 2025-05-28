package com.example.demo.model.Specifications;

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

    public static Specification<ContenidoEntity> estado(Integer estado)
    {
        return ((root, query, criteriaBuilder) ->
                estado == null ? null :
                        criteriaBuilder.equal(root.get("estado"), estado)
        );
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
