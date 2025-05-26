package com.example.demo.model.services.subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.mappers.subs.OfertaMapper;
import com.example.demo.model.repositories.subs.OfertaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OfertaService {
    private final OfertaRepository ofertaRepository;
    private final OfertaMapper ofertaMapper;

    public OfertaService(OfertaRepository ofertaRepository, OfertaMapper ofertaMapper) {
        this.ofertaRepository = ofertaRepository;
        this.ofertaMapper = ofertaMapper;
    }

    public Page<OfertaDTO> findAll(Pageable pageable) {
        return ofertaRepository.findAll(pageable).map(ofertaMapper::convertToDTO);
    }

    public Page<OfertaDTO> findActivos(Pageable pageable) {
        return ofertaRepository.findActivos(LocalDate.now(), pageable).map(ofertaMapper::convertToDTO);
    }


}
