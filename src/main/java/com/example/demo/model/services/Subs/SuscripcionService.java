package com.example.demo.model.services.Subs;

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
import com.example.demo.model.services.Email.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanService planService;
    private final SuscripcionMapper suscripcionMapper;
    private final EmailService emailService;


    public SuscripcionService(SuscripcionRepository suscripcionRepository, PlanRepository planRepository, UsuarioRepository usuarioRepository, PlanService planService, SuscripcionMapper suscripcionMapper, EmailService emailService) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.usuarioRepository = usuarioRepository;
        this.planService = planService;
        this.suscripcionMapper = suscripcionMapper;
        this.emailService = emailService;
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
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(()-> new SubNotFoundException("No se encontro la suscripcion"));
        return suscripcion;
    }

    //crear sub nueva
    public SuscripcionEntity save(Long id_usuario, TipoSuscripcion dato) {

        //buscamos el user
        UsuarioEntity usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(()-> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if(usuario.getSuscripcion() != null){
            throw new SubAlreadyExistException("Este usuario ya cuenta con suscripcion");
        }

        //buscamos el plan
        PlanSuscripcionEntity plan = planRepository.findByTipo(dato)
                .orElseThrow(() -> new PlanNotFoundException("no se enocntro el plan"));

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
        suscripcion.setFecha_fin(calcularFechaFin(dato));
        suscripcionRepository.save(suscripcion);
        usuario.setSuscripcion(suscripcion);
        return suscripcion;
    }

    //renovamos la sub si se esta por vencer
    public SuscripcionEntity renovar (Long id){
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(()-> new SubNotFoundException("Suscripcion no encontrada"));

        if(suscripcion.getFecha_inicio().isBefore(suscripcion.getFecha_fin())){
            suscripcion.setEstado(true);
        }else{
            suscripcion.setEstado(false);
        }
        suscripcion.setFecha_fin(calcularFechaFin(suscripcion.getPlan().getTipo()));
        suscripcionRepository.save(suscripcion);
        return suscripcion;
    }

    //indica cuando se terminara la sub segun el tipo
    private LocalDate calcularFechaFin(TipoSuscripcion tipo) {
        return switch (tipo) {
            case MENSUAL -> LocalDate.now().plusMonths(1);
            case SEMESTRAL -> LocalDate.now().plusMonths(6);
            case ANUAL -> LocalDate.now().plusYears(1);
        };
    }

    //listar activos
    public Page<SuscripcionDTO> mostrarActivos(Pageable pageable) {
        return suscripcionRepository.findActivos(pageable)
                .map(suscripcionMapper::convertToDTO);
    }

    //activar la sub cuando se concrete el pago
    public void activarSuscripion(Long id){
        suscripcionRepository.activarSub(id);
    }

}
