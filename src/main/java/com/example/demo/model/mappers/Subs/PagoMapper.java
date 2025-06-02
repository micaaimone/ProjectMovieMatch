package com.example.demo.model.mappers.Subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.entities.subs.PagoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {
    private final ModelMapper modelMapper;

    public PagoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PagoDTO convertToDTO(PagoEntity pagoEntity) {
        return modelMapper.map(pagoEntity, PagoDTO.class);
    }

    public PagoEntity convertToEntity(PagoDTO pagoDTO) {
        return modelMapper.map(pagoDTO, PagoEntity.class);
    }
}
