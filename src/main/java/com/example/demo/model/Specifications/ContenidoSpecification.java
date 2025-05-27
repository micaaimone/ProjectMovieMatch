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
                        criteriaBuilder.equal(root.get("titulo"), titulo)
        );
    }

    public static Specification<ContenidoEntity> puntuacion(Double puntuacion)
    {
        return ((root, query, criteriaBuilder) ->
                puntuacion == null ? null :
                        criteriaBuilder.lessThanOrEqualTo(root.get("puntuacion"), puntuacion)
        );
    }

//    public static Specification<ContenidoEntity> clasificacion(Double puntuacion)
//    {
//        return ((root, query, criteriaBuilder) ->
//                puntuacion == null ? null :
//                        criteriaBuilder.lessThanOrEqualTo(root.get("puntuacion"), puntuacion)
//        );
//    }


}
