package com.example.demo.model.mappers.Subs;

import com.example.demo.model.DTOs.subs.PlanDTO;
import com.example.demo.model.entities.subs.PlanSuscripcionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {
    private final ModelMapper modelMapper;
    public PlanMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PlanDTO convertToDTO(PlanSuscripcionEntity planSuscripcionEntity) {
        return modelMapper.map(planSuscripcionEntity, PlanDTO.class);
    }

    public PlanSuscripcionEntity convertToEntity(PlanDTO planDTO) {
        return modelMapper.map(planDTO, PlanSuscripcionEntity.class);
    }
}
