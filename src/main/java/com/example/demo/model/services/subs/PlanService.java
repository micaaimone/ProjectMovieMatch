package com.example.demo.model.services.subs;

import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.repositories.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<PlanSuscripcionEntity> verTodos(){
        return planRepository.findAll();
    }

    public void crearPlan(float precio, TipoSuscripcion tipo){
        PlanSuscripcionEntity plan = new PlanSuscripcionEntity();
        plan.setTipo(tipo);
        plan.setPrecio(precio);

        planRepository.save(plan);
    }

    public void cambiarMontoPlan(float montoNuevo, Long id){
        planRepository.actualizarPrecio(id, montoNuevo);
    }

    public float precioFinal (float monto, float desc){
        if(desc != 0){
            float extra=(monto * desc)/100;
            monto-=extra;
        }
        return monto;
    }
}

