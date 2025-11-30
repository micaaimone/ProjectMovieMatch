package com.example.demo.model.services.Subs;

import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.exceptions.SuscripcionException.PlanNotFoundException;
import com.example.demo.model.mappers.Subs.PlanMapper;
import com.example.demo.model.repositories.Subs.PlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public void cambiarMontoPlan(TipoSuscripcion tipo, BigDecimal montoNuevo){
        PlanSuscripcionEntity plan = planRepository.findByTipo(tipo)
                .orElseThrow(() -> new PlanNotFoundException("Plan no encontrado"));

        plan.setPrecio(montoNuevo);
        planRepository.save(plan);
    }

    public BigDecimal precioFinal (BigDecimal monto, BigDecimal desc){
        if (desc.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal extra = monto
                    .multiply(desc)
                    .divide(BigDecimal.valueOf(100));

            monto = monto.subtract(extra);
        }

        return monto;
    }
}

