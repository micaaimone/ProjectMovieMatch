package com.example.demo.model.Specifications.Contenido;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Subquery;
import com.example.demo.model.entities.User.ContenidoLikeEntity;


public class ContenidoSpecification {

    public static Specification<ContenidoEntity> genero(String genero)
    {
        return ((root, query, criteriaBuilder) -> {
            if (genero == null || genero.isEmpty()) {
                return null;
            }

            // Dividir por coma o espacio
            String[] generos = genero.split("[,\\s]+");

            // Crear una condición AND para cada género
            // (El contenido debe tener TODOS los géneros especificados)
            List<Predicate> predicates = new ArrayList<>();

            for (String g : generos) {
                if (!g.trim().isEmpty()) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("genero")),
                                    "%" + g.trim().toLowerCase() + "%"
                            )
                    );
                }
            }

            // Combinar todos con AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<ContenidoEntity> id(Long id)
    {
        return ((root, query, criteriaBuilder) ->
                id == null ? null :
                        criteriaBuilder.equal(root.get("id_contenido"), id)
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
                        criteriaBuilder.greaterThanOrEqualTo(root.get("puntuacion"), puntuacion)
        );
    }

    public static Specification<ContenidoEntity> clasificacion(String clasificacion)
    {
        return ((root, query, criteriaBuilder) -> {
            if (clasificacion == null || clasificacion.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("clasificacion")),
                    "%" + clasificacion.toLowerCase() + "%"
            );
        });
    }

    //specification de edad que nos sirve para recomendaciones
    public static Specification<ContenidoEntity> clasificacionPorEdad(Integer edad) {
        return (root, query, criteriaBuilder) -> {
            if (edad == null) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            // Para menores de 13 años: solo G y PG
            if (edad < 13) {
                predicates.add(criteriaBuilder.like(root.get("clasificacion"), "%G%"));
                predicates.add(criteriaBuilder.like(root.get("clasificacion"), "%PG%"));
            }
            // Para menores de 17 años: G, PG y PG-13
            else if (edad < 17) {
                predicates.add(criteriaBuilder.like(root.get("clasificacion"), "%G%"));
                predicates.add(criteriaBuilder.like(root.get("clasificacion"), "%PG%"));
                predicates.add(criteriaBuilder.like(root.get("clasificacion"), "%PG-13%"));
            }
            // Para mayores de 17 todo el contenido (sin restricciones)
            else {
                return null;
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    //specificacion para poder excluir los contenidos que el usuario ya le dio mg o vio
    public static Specification<ContenidoEntity> excluirContenidosConLike(Long usuarioId) {
        return (root, query, criteriaBuilder) -> {
            if (usuarioId == null) {
                return null;
            }

            // Subquery para obtener IDs de contenidos con like del usuario
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ContenidoLikeEntity> likeRoot = subquery.from(ContenidoLikeEntity.class);

            subquery.select(likeRoot.get("contenido").get("id_contenido"))
                    .where(criteriaBuilder.equal(
                            likeRoot.get("usuario").get("id"),
                            usuarioId
                    ));

            // Excluir esos IDs
            return criteriaBuilder.not(root.get("id_contenido").in(subquery));
        };
    }

}
