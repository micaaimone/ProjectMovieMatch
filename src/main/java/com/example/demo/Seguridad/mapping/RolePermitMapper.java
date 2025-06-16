package com.example.demo.Seguridad.mapping;

import com.example.demo.Seguridad.Enum.Permit;
import com.example.demo.Seguridad.Enum.Role;
import org.springframework.stereotype.Component;

import java.util.*;

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
                Permit.USUARIO_VER_LIKES,
                Permit.CREAR_GRUPO,
                Permit.VER_GRUPO,
                Permit.ENVIAR_SOLICITUD_AMISTAD,
                Permit.ACEPTAR_SOLICITUD_AMISTAD,
                Permit.RECHAZAR_SOLICITUD_AMISTAD,
                Permit.BLOQUEAR_USUARIO,
                Permit.CANCELAR_SOLICITUD,
                Permit.VER_SOLICITUD,
                Permit.VER_SOLICITUDES,
                Permit.VER_AMIGOS,
                Permit.ELIMINAR_AMIGO,
                Permit.VER_COINCIDENCIAS
                Permit.USUARIO_VER_LIKES,
                Permit.USUARIO_SOLICITAR_SOPORTE
        ));

        // ROLE_PREMIUM hereda de USER + permisos adicionales
        Set<Permit> premiumPermits = new HashSet<>(rolePermissions.get(Role.ROLE_USER));
        premiumPermits.addAll(Set.of(
                Permit.GENERO_ILIMITADO,
                Permit.MODIFICAR_GRUPO,
                Permit.AGREGAR_USUARIO_A_GRUPO,
                Permit.ELIMINAR_USUARIO_DE_GRUPO
        ));
        rolePermissions.put(Role.ROLE_PREMIUM, Set.copyOf(premiumPermits));

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
                Permit.USUARIO_LISTAR_DESACTIVADOS,
                Permit.CREAR_GRUPO,
                Permit.VER_GRUPO,
                Permit.MODIFICAR_GRUPO,
                Permit.AGREGAR_USUARIO_A_GRUPO,
                Permit.ELIMINAR_USUARIO_DE_GRUPO,
                Permit.ELIMINAR_GRUPO,
                Permit.ENVIAR_SOLICITUD_AMISTAD,
                Permit.ACEPTAR_SOLICITUD_AMISTAD,
                Permit.RECHAZAR_SOLICITUD_AMISTAD,
                Permit.BLOQUEAR_USUARIO,
                Permit.CANCELAR_SOLICITUD,
                Permit.VER_SOLICITUD,
                Permit.VER_SOLICITUDES,
                Permit.VER_AMIGOS,
                Permit.ELIMINAR_AMIGO,
                Permit.VER_COINCIDENCIAS
                Permit.USUARIO_LISTAR_DESACTIVADOS,
                Permit.USUARIO_ENVIAR_ANUNCIO
        ));
    }

    public Set<Permit> getPermissionsForRole(Role role) {
        return rolePermissions.getOrDefault(role, Set.of());
    }
}
