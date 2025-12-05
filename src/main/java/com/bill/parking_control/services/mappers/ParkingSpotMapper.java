package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.spot.ParkingSpotCreateDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotResponseDTO;
import com.bill.parking_control.dtos.spot.ParkingSpotUpdateDto;
import com.bill.parking_control.persistences.entities.ParkingSpot;

@Component
public class ParkingSpotMapper {
    public ParkingSpot toEntity(ParkingSpotCreateDTO dto) {
        return ParkingSpot.builder()
                .code(dto.code())
                .type(dto.type())
                .status(dto.status())
                .covered(dto.covered())
                .build();
    }

    public ParkingSpotResponseDTO toResponseDTO(ParkingSpot entity) {
        return new ParkingSpotResponseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getType(),
                entity.getStatus(),
                entity.isCovered());
    }

    public void updateEntity(ParkingSpot entity, ParkingSpotUpdateDto dto) {
        if (dto.code() != null) {
            entity.setCode(dto.code());
        }
        if (dto.vehicleType() != null) {
            entity.setType(dto.vehicleType());
        }
        if (dto.spotStatus() != null) {
            entity.setStatus(dto.spotStatus());
        }
        entity.setCovered(dto.covered());
    }
}
