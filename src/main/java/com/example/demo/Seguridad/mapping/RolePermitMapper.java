package com.example.demo.Seguridad.mapping;

import com.example.demo.Seguridad.Enum.Permit;
import com.example.demo.Seguridad.Enum.Role;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class RolePermitMapper {


    private final Map<Role, Set<Permit>> rolePermissions = new EnumMap<>(Role.class);

    public RolePermitMapper() {
        initializeMappings();
    }

    private void initializeMappings() {
        rolePermissions.put(Role.ROLE_USER, Set.of(
                Permit.SERIES_READ,
                Permit.VER_PELICULAS,
                Permit.VER_CONTENIDO_ACTIVO,
                Permit.CREAR_RESENIA,
                Permit.ELIMINAR_RESENIA,
                Permit.ELIMINAR_RESENIA_POR_USUARIO_Y_CONTENIDO,
                Permit.VER_RESENIAS_POR_USUARIO,
                Permit.MODIFICAR_RESENIA,
                Permit.LISTA_CREAR,
                Permit.LISTA_AGREGAR_CONTENIDO,
                Permit.LISTA_VER_TODAS,
                Permit.LISTA_VER_DETALLE,
                Permit.LISTA_CAMBIAR_NOMBRE,
                Permit.LISTA_CAMBIAR_ESTADO,
                Permit.LISTA_ELIMINAR_CONTENIDO,
                Permit.LISTA_ELIMINAR,
                Permit.LISTA_VER_PUBLICAS,
                Permit.SUSCRIPCION_CREAR,
                Permit.SUSCRIPCION_RENOVAR,
                Permit.SUSCRIPCION_VER_POR_ID,
                Permit.PLAN_VER,
                Permit.OFERTA_VER_ACTIVAS,
                Permit.PAGO_VER_POR_SUSCRIPCION,
                Permit.PAGO_VER_POR_ID,
                Permit.USUARIO_VER,
                Permit.USUARIO_MODIFICAR,
                Permit.USUARIO_LIKE,
                Permit.USUARIO_QUITAR_LIKE,
                Permit.USUARIO_VER_LIKES
        ));

        rolePermissions.put(Role.ROLE_PREMIUM, rolePermissions.get(Role.ROLE_USER));

        rolePermissions.put(Role.ROLE_ADMIN, Set.of(
                Permit.SERIES_READ,
                Permit.SERIES_WRITE,
                Permit.VER_PELICULAS,
                Permit.VER_CONTENIDO_ACTIVO,
                Permit.VER_CONTENIDO_BAJA,
                Permit.ACTIVAR_CONTENIDO,
                Permit.DESACTIVAR_CONTENIDO,
                Permit.BUSCAR_NUEVO_CONTENIDO_POR_NOMBRE,
                Permit.CREAR_RESENIA,
                Permit.ELIMINAR_RESENIA,
                Permit.ELIMINAR_RESENIA_POR_USUARIO_Y_CONTENIDO,
                Permit.VER_RESENIAS_POR_USUARIO,
                Permit.MODIFICAR_RESENIA,
                Permit.LISTA_CREAR,
                Permit.LISTA_AGREGAR_CONTENIDO,
                Permit.LISTA_VER_TODAS,
                Permit.LISTA_VER_DETALLE,
                Permit.LISTA_CAMBIAR_NOMBRE,
                Permit.LISTA_CAMBIAR_ESTADO,
                Permit.LISTA_ELIMINAR_CONTENIDO,
                Permit.LISTA_ELIMINAR,
                Permit.LISTA_VER_PUBLICAS,
                Permit.SUSCRIPCION_CREAR,
                Permit.SUSCRIPCION_RENOVAR,
                Permit.SUSCRIPCION_VER_TODAS,
                Permit.SUSCRIPCION_VER_ACTIVAS,
                Permit.SUSCRIPCION_VER_POR_ID,
                Permit.PLAN_VER,
                Permit.PLAN_EDITAR,
                Permit.OFERTA_CREAR,
                Permit.OFERTA_RENOVAR,
                Permit.OFERTA_VER_TODAS,
                Permit.OFERTA_VER_ACTIVAS,
                Permit.PAGO_VER_TODOS,
                Permit.PAGO_VER_POR_SUSCRIPCION,
                Permit.PAGO_VER_POR_ID,
                Permit.USUARIO_VER,
                Permit.USUARIO_MODIFICAR,
                Permit.USUARIO_REACTIVAR,
                Permit.USUARIO_DESACTIVAR,
                Permit.USUARIO_LIKE,
                Permit.USUARIO_QUITAR_LIKE,
                Permit.USUARIO_VER_LIKES,
                Permit.USUARIO_LISTAR_ACTIVOS,
                Permit.USUARIO_LISTAR_DESACTIVADOS
        ));
    }

    public Set<Permit> getPermissionsForRole(Role role) {
        return rolePermissions.getOrDefault(role, Set.of());
    }
}
