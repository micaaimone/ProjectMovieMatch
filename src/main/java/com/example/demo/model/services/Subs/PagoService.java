package com.example.demo.model.services.Subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.entities.subs.PagoEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import com.example.demo.model.exceptions.SuscripcionException.PagoNotFoundException;
import com.example.demo.model.mappers.Subs.PagoMapper;
import com.example.demo.model.repositories.Subs.PagoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    public PagoService(PagoRepository pagoRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
    }

    public void anadirPago(SuscripcionEntity suscripcion, BigDecimal monto) {
        pagoRepository.save(new PagoEntity(null, "mercado pago", LocalDateTime.now(), monto, suscripcion));
    }

    public Page<PagoDTO> findAll(Pageable pageable) {
        return pagoRepository.findAll(pageable)
                .map(pagoMapper::convertToDTO);
    }

    public Page<PagoDTO> findBySub(Long id, Pageable pageable) {
        return pagoRepository.findBySuscripcionId(id, pageable)
                .map(pagoMapper::convertToDTO);
    }

    public PagoDTO findById(Long id) {
        PagoEntity pagoEntity = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException("Pago no encontrado"));

        return pagoMapper.convertToDTO(pagoEntity);
    }
}
