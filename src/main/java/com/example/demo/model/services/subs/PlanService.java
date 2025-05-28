package com.example.demo.model.services.subs;

import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.mappers.subs.PlanMapper;
import com.example.demo.model.repositories.subs.PlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public PlanService(PlanRepository planRepository, PlanMapper planMapper) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }



    public Page<PlanDTO> verTodos(Pageable pageable){
        return planRepository.findAll(pageable)
                .map(planMapper::convertToDTO);
    }


    public Optional<OfertaEntity> getOfertaActiva(PlanSuscripcionEntity plan) {
        LocalDate hoy = LocalDate.now();
        return plan.getOfertas().stream()
                .filter(oferta -> !hoy.isBefore(oferta.getFecha_inicio()) && !hoy.isAfter(oferta.getFecha_fin()))
                .findFirst();
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

