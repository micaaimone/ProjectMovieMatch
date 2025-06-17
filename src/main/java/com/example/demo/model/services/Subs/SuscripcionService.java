package com.example.demo.model.services.Subs;

import com.example.demo.Seguridad.Entities.CredentialsEntity;
import com.example.demo.Seguridad.Entities.RoleEntity;
import com.example.demo.Seguridad.Enum.Role;
import com.example.demo.Seguridad.repositories.CredentialsRepository;
import com.example.demo.Seguridad.repositories.RoleRepository;
import com.example.demo.model.DTOs.subs.SuscripcionDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.exceptions.SuscripcionException.PlanNotFoundException;
import com.example.demo.model.exceptions.SuscripcionException.SubAlreadyExistException;
import com.example.demo.model.exceptions.SuscripcionException.SubNotFoundException;
import com.example.demo.model.exceptions.UsuarioExceptions.UsuarioNoEncontradoException;
import com.example.demo.model.mappers.Subs.SuscripcionMapper;
import com.example.demo.model.repositories.Subs.PlanRepository;
import com.example.demo.model.repositories.Subs.SuscripcionRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanService planService;
    private final SuscripcionMapper suscripcionMapper;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;


    public SuscripcionService(SuscripcionRepository suscripcionRepository, PlanRepository planRepository, UsuarioRepository usuarioRepository,
                              PlanService planService, SuscripcionMapper suscripcionMapper, CredentialsRepository credentialsRepository, RoleRepository roleRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.usuarioRepository = usuarioRepository;
        this.planService = planService;
        this.suscripcionMapper = suscripcionMapper;
        this.credentialsRepository = credentialsRepository;
        this.roleRepository = roleRepository;

    }

    //mostrar todos
    public Page<SuscripcionDTO> findAll(Pageable pageable) {
        return suscripcionRepository.findAll(pageable)
                .map(suscripcionMapper::convertToDTO);
    }

    public SuscripcionDTO findById(Long id) {
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(()-> new SubNotFoundException("No se encontro la suscripcion"));
        return suscripcionMapper.convertToDTO(suscripcion);
    }

    public SuscripcionEntity findByIdEntity(Long id) {
        return suscripcionRepository.findById(id)
                .orElseThrow(()-> new SubNotFoundException("No se encontro la suscripcion"));
    }

    //crear sub nueva
    public SuscripcionEntity save(Long id_usuario, TipoSuscripcion dato) {

        //buscamos el user
        UsuarioEntity usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(()-> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if(usuario.getSuscripcion()!=null){
            throw new SubAlreadyExistException("Este usuario ya cuenta con suscripcion");
        }

        //buscamos el plan
        PlanSuscripcionEntity plan = planRepository.findByTipo(dato)
                .orElseThrow(() -> new PlanNotFoundException("no se encontro el plan"));

        //buscamos oferta activa
        Optional<OfertaEntity> oferta = planService.getOfertaActiva(plan);


        //creamos la sub
        SuscripcionEntity suscripcion = new SuscripcionEntity();
        //todaia no esta activada hasta que se concrete el pago
        suscripcion.setEstado(false);

        float precioFinal = oferta
                .map(ofertaEntity -> planService.precioFinal(plan.getPrecio(), ofertaEntity.getDescuento()))
                .orElse(plan.getPrecio());


        suscripcion.setMonto(precioFinal);
        suscripcion.setPlan(plan);
        suscripcion.setUsuario(usuario);
        suscripcion.setFecha_inicio(LocalDate.now());
        suscripcion.setFecha_fin(LocalDate.now());
        suscripcionRepository.save(suscripcion);
        usuario.setSuscripcion(suscripcion);
        return suscripcion;
    }

    //renovamos la sub si se esta por vencer
    //creo q no funcionaria asi
    public SuscripcionEntity renovar (Long id){
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(()-> new SubNotFoundException("Suscripcion no encontrada"));


        suscripcion.setFecha_fin(calcularFechaFin(suscripcion.getFecha_fin(), suscripcion.getPlan().getTipo()));
        suscripcionRepository.save(suscripcion);
        return suscripcion;
    }

    //indica cuando se terminara la sub segun el tipo
    private LocalDate calcularFechaFin(LocalDate fecha, TipoSuscripcion tipo) {
        switch (tipo) {
            case MENSUAL -> fecha = fecha.plusMonths(1);
            case SEMESTRAL -> fecha = fecha.plusMonths(6);
            case ANUAL -> fecha = fecha.plusYears(1);
        }
        return fecha;
    }

    //listar activos
    public Page<SuscripcionDTO> mostrarActivos(Pageable pageable) {
        return suscripcionRepository.findActivos(pageable)
                .map(suscripcionMapper::convertToDTO);
    }

    //activar la sub cuando se concrete el pago y cambiamos el rol
    public void activarSuscripion(Long id){
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new SubNotFoundException("No se encontro la suscripcion"));

        UsuarioEntity user = usuarioRepository.findById(suscripcion.getUsuario().getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro el usuario"));

        // Buscamos las credenciales asociadas
        CredentialsEntity credencial = credentialsRepository.findById(suscripcion.getUsuario().getCredencial().getId())
                .orElseThrow(() -> new RuntimeException("Credencial no encontrada"));


        // Buscamos el rol PREMIUM
        RoleEntity rolPremium = roleRepository.findByRole(Role.ROLE_PREMIUM)
                .orElseThrow(() -> new RuntimeException("Rol PREMIUM no encontrado"));


        if (credencial.getRoles().contains(rolPremium)) {
            // Ya es premium
            suscripcion.setFecha_fin(calcularFechaFin(suscripcion.getFecha_fin(), suscripcion.getPlan().getTipo()));
            suscripcion.setEstado(true);
            suscripcionRepository.save(suscripcion);

            return;
        }

        Set<RoleEntity> nuevosRoles = new HashSet<>(credencial.getRoles());
        nuevosRoles.add(rolPremium);
        credencial.setRoles(nuevosRoles);

        credentialsRepository.save(credencial);

        suscripcion.setFecha_fin(calcularFechaFin(suscripcion.getFecha_fin(), suscripcion.getPlan().getTipo()));
        suscripcion.setEstado(true);
        suscripcionRepository.save(suscripcion);
        user.setSuscripcion(suscripcion);
        usuarioRepository.save(user);

    }

    //desactivamos sub si vencio y cambiamos el rol
    public void desactivarSuscripion(){
        List<SuscripcionEntity> subs = suscripcionRepository.bajarSub(LocalDate.now());

        if(subs.isEmpty()){
            return;
        }
        List<CredentialsEntity> credenciales = subs.stream()
                .map(suscripcionEntity -> suscripcionEntity.getUsuario().getCredencial())
                .collect(Collectors.toList());

        // Buscamos el rol PREMIUM
        RoleEntity rolPremium = roleRepository.findByRole(Role.ROLE_PREMIUM)
                .orElseThrow(() -> new RuntimeException("Rol PREMIUM no encontrado"));

        credenciales.forEach(credentialsEntity -> credentialsEntity.getRoles().remove(rolPremium));

        credentialsRepository.saveAll(credenciales);

        subs.forEach(suscripcionEntity -> suscripcionEntity.setEstado(false));

        suscripcionRepository.saveAll(subs);

    }

    public List<SuscripcionEntity> avisarVencimiento(){
        return suscripcionRepository.porVencer(LocalDate.now().plusDays(5));
    }

    //cambiar de plan
    public SuscripcionEntity cambiarPlan(UsuarioEntity usuario, TipoSuscripcion tipo){
        SuscripcionEntity sub = suscripcionRepository.findById(usuario.getSuscripcion().getId_suscripcion())
                .orElseThrow(() -> new SubNotFoundException("No Cuenta con una suscripcion"));

        PlanSuscripcionEntity plan = planRepository.findByTipo(tipo).orElseThrow(()-> new PlanNotFoundException("No se encontro el plan"));

        //buscamos oferta activa
        Optional<OfertaEntity> oferta = planService.getOfertaActiva(plan);

        float precioFinal = oferta
                .map(ofertaEntity -> planService.precioFinal(plan.getPrecio(), ofertaEntity.getDescuento()))
                .orElse(plan.getPrecio());

        sub.setMonto(precioFinal);
        sub.setPlan(plan);
        sub.setFecha_fin(calcularFechaFin(sub.getFecha_fin(), tipo));
        suscripcionRepository.save(sub);

        return sub;
    }
}
