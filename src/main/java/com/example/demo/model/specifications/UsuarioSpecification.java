package com.example.demo.model.specifications;

import com.example.demo.model.entities.UsuarioEntity;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    public static Specification<UsuarioEntity> nombre(String nombre)
    {
        return ((root, query, criteriaBuilder) ->
                nombre == null ? null :
                        criteriaBuilder.equal(root.get("nombre"), nombre)
        );
    }

    public static Specification<UsuarioEntity> apellido(String apellido)
    {
        return ((root, query, criteriaBuilder) ->
                apellido == null ? null :
                        criteriaBuilder.equal(root.get("apellido"), apellido)
        );
    }

    public static Specification<UsuarioEntity> email(String email)
    {
        return ((root, query, criteriaBuilder) ->
                email == null ? null :
                        criteriaBuilder.equal(root.get("email"), email)
        );
    }

    public static Specification<UsuarioEntity> username(String username)
    {
        return ((root, query, criteriaBuilder) ->
                username == null ? null :
                        criteriaBuilder.equal(root.get("username"), username)
        );
    }


}
