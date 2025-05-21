package com.example.demo.model.services.subs;

import com.example.demo.model.entities.UsuarioEntity;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.repositories.OfertaRepository;
import com.example.demo.model.repositories.PlanRepository;
import com.example.demo.model.repositories.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionService { private final SuscripcionRepository suscripcionRepository;

    private final PlanRepository planRepository;
    private final OfertaRepository ofertaRepository;
   // private final UsuarioRepository usuarioRepository;
    private final PlanService planService;

    public SuscripcionService(SuscripcionRepository suscripcionRepository, PlanRepository planRepository, OfertaRepository ofertaRepository,/* UsuarioRepository usuarioRepository,*/ PlanService planService) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.ofertaRepository = ofertaRepository;
       // this.usuarioRepository = usuarioRepository;
        this.planService = planService;
    }


    public List<SuscripcionEntity> findAll() {
        return suscripcionRepository.findAll();
    }

    public Optional<SuscripcionEntity> findById(Long id) {
        return suscripcionRepository.findById(id);
    }

    public void delete(SuscripcionEntity dato) {
        suscripcionRepository.delete(dato);
    }

    //
    public String save(Long id_usuario, TipoSuscripcion dato) {

        //UsuarioEntity usuario = usuarioRepository.findById(id_usuario)
            //    .orElseThrow(() -> new RuntimeException("No se encontró el usuario"));

        //buscamos el plan
        PlanSuscripcionEntity plan = planRepository.findByTipo(dato)
                .orElseThrow(() -> new RuntimeException("no se enocntro el plan"));

        //buscamos oferta activa
        OfertaEntity oferta = ofertaRepository.buscarOferta(plan.getId(), LocalDate.now());


        //creamos la sub
        SuscripcionEntity suscripcion = new SuscripcionEntity();
        //todaia no esta activada hasta que se concrete el pago
        suscripcion.setEstado(false);

        if(oferta != null){
            suscripcion.setMonto(planService.precioFinal(plan.getPrecio(), oferta.getDescuento()));
        }else{
            suscripcion.setMonto(plan.getPrecio());
        }

        suscripcion.setPlan(plan);
        //suscripcion.setUsuario(usuario);
        suscripcion.setFecha_inicio(LocalDate.now());
        suscripcion.setFecha_fin(calcularFechaFin(dato));

        //creamos el pago
        suscripcionRepository.save(suscripcion);


        return null;//mercadoPagoService.crearPreferenciaDePrueba(dato.name(), suscripcion.getMonto());
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
            System.out.println("⛔ La suscripción ya venció.");
            sub.setEstado(false);
            suscripcionRepository.save(sub);
        }else{
            System.out.println("✅ La suscripción aún está activa.");
        }
    }

    //listar activos
    public List<SuscripcionEntity> findActivos(){
        return suscripcionRepository.findActivos();
    }

    //activar la sub cuando se concrete el pago
    public void activarSuscripion(Long id){
        suscripcionRepository.activarSub(id);
    }

}
