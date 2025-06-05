package com.example.demo.model.services.Subs;

import com.example.demo.model.DTOs.MailDTO;
import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.entities.subs.OfertaEntity;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import com.example.demo.model.entities.subs.TipoSuscripcion;
import com.example.demo.model.exceptions.SuscripcionException.OfertaNotFoundException;
import com.example.demo.model.exceptions.SuscripcionException.PlanNotFoundException;
import com.example.demo.model.mappers.Subs.OfertaMapper;
import com.example.demo.model.repositories.Subs.OfertaRepository;
import com.example.demo.model.repositories.Subs.PlanRepository;
import com.example.demo.model.services.Email.EmailService;
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
    private final EmailService emailService;

    public OfertaService(OfertaRepository ofertaRepository, OfertaMapper ofertaMapper, PlanRepository planRepository, EmailService emailService) {
        this.ofertaRepository = ofertaRepository;
        this.ofertaMapper = ofertaMapper;
        this.planRepository = planRepository;
        this.emailService = emailService;
    }

    public void CrearOferta(OfertaDTO ofertaDTO, TipoSuscripcion tipoSuscripcion) {
        PlanSuscripcionEntity plan = planRepository.findByTipo(tipoSuscripcion).
                orElseThrow(() -> new PlanNotFoundException("Plan no encontrado"));
        OfertaEntity oferta = ofertaMapper.convertToEntity(ofertaDTO);
        oferta.setFecha_inicio(LocalDate.now());
        oferta.setFecha_fin(LocalDate.now().plusMonths(1));
        oferta.setPlan(plan);
        ofertaRepository.save(oferta);
        // enviamos mail
        avisarOferta(oferta);
    }

    public void renovarOferta(Long id) {
        OfertaEntity oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new OfertaNotFoundException("Oferta no encontrada"));
        oferta.setFecha_fin(oferta.getFecha_fin().plusMonths(1));
        ofertaRepository.save(oferta);
    }

    public Page<OfertaDTO> findAll(Pageable pageable) {
        return ofertaRepository.findAll(pageable).map(ofertaMapper::convertToDTO);
    }

    public Page<OfertaDTO> findActivos(Pageable pageable) {
        return ofertaRepository.findActivos(LocalDate.now(), pageable).map(ofertaMapper::convertToDTO);
    }


    //notificar a usuarios que hay ofertas
    public void avisarOferta(OfertaEntity oferta) {
        MailDTO dto = MailDTO.builder()
                .subject("Nuevas Ofertas!")
                .mensaje("Buenas tardes usuarios, tenemos sorpresa" +
                        "Una nueva oferta para el plan "+ oferta.getPlan().getTipo() +
                        " de un "+ oferta.getDescuento() + "% de descuento \n" +
                        "Esperamos que la disfruten. \n Atte: Movie-Match")
                .build();
        emailService.SendMailToAll(dto);

    }

}
