package com.bill.parking_control.services.mappers;

import org.springframework.stereotype.Component;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.dtos.tariff.TariffUpdateDto;
import com.bill.parking_control.persitenses.entities.Tariff;

@Component
public class TariffMapper {
    public TariffResponseDTO mapToDTO(Tariff tariff) {
        return new TariffResponseDTO(
                tariff.getId(),
                tariff.getVehicleType(),
                tariff.getHourlyRate(),
                tariff.getDailyRate(),
                tariff.getMonthlyRate(),
                tariff.getValidFrom(),
                tariff.getValidUntil(),
                tariff.isActive());
    }

    public Tariff mapToEntity(TariffCreateDTO dto) {
        return Tariff.builder()
                .vehicleType(dto.vehicleType())
                .hourlyRate(dto.hourlyRate())
                .dailyRate(dto.dailyRate())
                .monthlyRate(dto.monthlyRate())
                .validFrom(dto.validFrom())
                .validUntil(dto.validUntil())
                .build();
    }

    public void updateEntity(Tariff tariff, TariffUpdateDto dto) {
        if (dto.vehicleType() != null) {
            tariff.setVehicleType(dto.vehicleType());
        }
        if (dto.hourlyRate() != null) {
            tariff.setHourlyRate(dto.hourlyRate());
        }
        if (dto.dailyRate() != null) {
            tariff.setDailyRate(dto.dailyRate());
        }
        if (dto.monthlyRate() != null) {
            tariff.setMonthlyRate(dto.monthlyRate());
        }
        if (dto.validFrom() != null) {
            tariff.setValidFrom(dto.validFrom());
        }
        if (dto.validUntil() != null) {
            tariff.setValidUntil(dto.validUntil());
        }
        tariff.setActive(dto.active());
    }
}
