package com.example.demo.model.services.subs;

import com.example.demo.model.DTOs.subs.SuscripcionDTO;
import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.mappers.subs.SuscripcionMapper;
import com.example.demo.model.repositories.subs.PlanRepository;
import com.example.demo.model.repositories.subs.SuscripcionRepository;
import com.example.demo.model.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.Optional;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanService planService;
    private final SuscripcionMapper suscripcionMapper;


    public SuscripcionService(SuscripcionRepository suscripcionRepository, PlanRepository planRepository,UsuarioRepository usuarioRepository, PlanService planService, SuscripcionMapper suscripcionMapper) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.usuarioRepository = usuarioRepository;
        this.planService = planService;
        this.suscripcionMapper = suscripcionMapper;

    }

//mostrar todos
    public Page<SuscripcionDTO> findAll(Pageable pageable) {
        return suscripcionRepository.findAll(pageable)
                .map(suscripcionMapper::convertToDTO);
    }

    public SuscripcionDTO findById(Long id) {
        SuscripcionEntity suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No se encontro el id del suscripcion"));
        return suscripcionMapper.convertToDTO(suscripcion);
    }


    public SuscripcionEntity save(Long id_usuario, TipoSuscripcion dato) {

        UsuarioEntity usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(()-> new RuntimeException("no se encontro el usuario"));

        //buscamos el plan
        PlanSuscripcionEntity plan = planRepository.findByTipo(dato)
                .orElseThrow(() -> new RuntimeException("no se enocntro el plan"));

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

    //verifica si la fecha de hoy es menor que la de vencimiento
    public boolean vencio(SuscripcionEntity suscripcion){
        LocalDate hoy = LocalDate.now();
        return hoy.isAfter(suscripcion.getFecha_fin());
    }

    //utiliza la verificacion y si caduco cambia el estado
    public void verificarVencimiento(Long id){
        SuscripcionEntity sub = suscripcionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("no existe la suscripcion"));

        if(vencio(sub)){
            sub.setEstado(false);
            suscripcionRepository.save(sub);
        }
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
