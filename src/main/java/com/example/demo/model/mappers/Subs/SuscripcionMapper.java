package com.example.demo.model.mappers.Subs;

import com.example.demo.model.DTOs.subs.PagoDTO;
import com.example.demo.model.DTOs.subs.SuscripcionDTO;
import com.example.demo.model.entities.subs.PagoEntity;
import com.example.demo.model.entities.subs.SuscripcionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuscripcionMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public SuscripcionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SuscripcionDTO convertToDTO(SuscripcionEntity suscripcionEntity) {
        SuscripcionDTO dto = modelMapper.map(suscripcionEntity, SuscripcionDTO.class);

        if(suscripcionEntity.getPagos() != null) {

            List<PagoDTO> pagosDTO = suscripcionEntity.getPagos()
                    .stream()
                    .map(pago -> modelMapper.map(pago, PagoDTO.class))
                    .toList();

            dto.setPagos(pagosDTO);
        }

        return dto;
    }

    public SuscripcionEntity convertToEntity(SuscripcionDTO dto) {
        SuscripcionEntity entity = modelMapper.map(dto, SuscripcionEntity.class);
        if(dto.getPagos() != null) {
            List<PagoEntity> pagoEntities = dto.getPagos()
                    .stream()
                    .map(pagoDTO -> modelMapper.map(pagoDTO, PagoEntity.class))
                    .toList();

            entity.setPagos(pagoEntities);
        }
        return entity;
    }
}
