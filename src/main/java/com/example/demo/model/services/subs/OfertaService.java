package com.example.demo.model.services.subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.mappers.subs.OfertaMapper;
import com.example.demo.model.repositories.subs.OfertaRepository;
import com.example.demo.model.repositories.subs.PlanRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OfertaService {
    private final OfertaRepository ofertaRepository;
    private final OfertaMapper ofertaMapper;
    private final PlanRepository planRepository;

    public OfertaService(OfertaRepository ofertaRepository, OfertaMapper ofertaMapper, PlanRepository planRepository) {
        this.ofertaRepository = ofertaRepository;
        this.ofertaMapper = ofertaMapper;
        this.planRepository = planRepository;
    }

    public ResponseEntity<Void> CrearOferta( String descripcion, float desc, Long idPlan) {
        OfertaEntity oferta = new OfertaEntity();
        oferta.setDescripcion(descripcion);
        oferta.setDescuento(desc);
        oferta.setFecha_inicio(LocalDate.now());
        oferta.setFecha_fin(LocalDate.now().plusMonths(1));
        oferta.setPlan(planRepository.findById(idPlan).
                orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado")));
        ofertaRepository.save(oferta);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> renovarOferta(Long id) {
        OfertaEntity oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        oferta.setFecha_fin(oferta.getFecha_fin().plusMonths(1));
        ofertaRepository.save(oferta);
        return ResponseEntity.ok().build();
    }

    public Page<OfertaDTO> findAll(Pageable pageable) {
        return ofertaRepository.findAll(pageable).map(ofertaMapper::convertToDTO);
    }

    public Page<OfertaDTO> findActivos(Pageable pageable) {
        return ofertaRepository.findActivos(LocalDate.now(), pageable).map(ofertaMapper::convertToDTO);
    }


}
