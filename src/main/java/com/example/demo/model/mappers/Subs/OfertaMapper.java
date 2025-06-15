package com.example.demo.model.mappers.Subs;

import com.example.demo.model.DTOs.subs.OfertaDTO;
import com.example.demo.model.entities.subs.OfertaEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfertaMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public OfertaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public OfertaDTO convertToDTO(OfertaEntity ofertaEntity) {
        return modelMapper.map(ofertaEntity, OfertaDTO.class);
    }

    public OfertaEntity convertToEntity(OfertaDTO ofertaDTO) {
        return modelMapper.map(ofertaDTO, OfertaEntity.class);
    }
}
