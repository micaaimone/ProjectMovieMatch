package com.example.demo.model.services.Grupo;

import com.example.demo.model.entities.Contenido.ContenidoEntity;
import com.example.demo.model.entities.GrupoEntity;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.repositories.Grupo.GrupoRepository;
import com.example.demo.model.repositories.Subs.SuscripcionRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final SuscripcionRepository suscripcionRepository;


    /**
     * Método para mostrar las películas según la cantidad de likes en el grupo.
     * Solo muestra las que tengan al menos 3 likes.
     * Si todos los usuarios son premium, muestra top 10; sino top 3.
     */
    public List<ContenidoEntity> mostrarPelis(Long idGrupo) {
        GrupoEntity grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        Set<UsuarioEntity> usuarios = grupo.getListaUsuarios();

        // Contar cuántos likes tiene cada película dentro del grupo
        Map<ContenidoEntity, Integer> conteoLikes = new HashMap<>();
        for (UsuarioEntity usuario : usuarios) {
            Set<ContenidoEntity> likes = usuario.getLikes();
            for (ContenidoEntity peli : likes) {
                conteoLikes.put(peli, conteoLikes.getOrDefault(peli, 0) + 1);
            }
        }

        // Filtrar las películas que tengan al menos 3 likes
        List<Map.Entry<ContenidoEntity, Integer>> peliculasConMatch = conteoLikes.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .sorted(Map.Entry.<ContenidoEntity, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        // Verificar si todos los usuarios son premium
        boolean todosPremium = usuarios.stream().allMatch(this::esPremium);

        int limite = todosPremium ? 10 : 3;

        // Limitar el top N y retornar solo las películas
        return peliculasConMatch.stream()
                .limit(limite)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para saber si un usuario es premium.
     * Verifica que la suscripción esté activa, y que el plan sea premium.
     */
    private boolean esPremium(UsuarioEntity usuario) {
        SuscripcionEntity suscripcion = usuario.getSuscripcion();
        if (suscripcion == null || !suscripcion.isEstado()) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        boolean dentroDeFechas =
                (suscripcion.getFecha_inicio() == null || !hoy.isBefore(suscripcion.getFecha_inicio())) &&
                        (suscripcion.getFecha_fin() == null || !hoy.isAfter(suscripcion.getFecha_fin()));

        if (!dentroDeFechas) {
            return false;
        }

        PlanSuscripcionEntity plan = suscripcion.getPlan();
        if (plan == null || plan.getTipo() == null) {
            return false;
        }

        return plan.getTipo().name().equalsIgnoreCase("premium");
    }
    // 1. Crear un grupo
    public GrupoEntity crearGrupo(String nombreGrupo, Long idAdministrador) {
        UsuarioEntity admin = UsuarioRepository.findById(idAdministrador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        GrupoEntity grupo = new GrupoEntity();
        grupo.setNombre(nombreGrupo);
        grupo.setAdministrador(admin);
        grupo.getListaUsuarios().add(admin); // El admin se agrega como usuario del grupo

        return grupoRepository.save(grupo);
    }

    public void unirseAGrupo(Long idGrupo, Long idUsuario) {
        GrupoEntity grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        UsuarioEntity usuario = UsuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        grupo.getListaUsuarios().add(usuario);
        grupoRepository.save(grupo);
    }

    public List<UsuarioEntity> MostrarIntegrantes(Long idGrupo) {
        GrupoEntity grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        return new ArrayList<>(grupo.getListaUsuarios());
    }





}


