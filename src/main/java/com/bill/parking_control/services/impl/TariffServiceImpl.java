package com.bill.parking_control.services.impl;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.Vehicle;
import com.bill.parking_control.persitenses.repositories.TariffRepository;
import com.bill.parking_control.services.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    @Override
    public TariffResponseDTO createTariff(TariffCreateDTO dto) {
        // Check if tariff exists for type, maybe update or throw. For now, just
        // create/overwrite.
        // Actually, let's check if there is an active one.
        // For simplicity, we assume one tariff per vehicle type.

        Tariff tariff = tariffRepository.findByVehicleType(dto.vehicleType())
                .orElse(new Tariff());

        tariff.setVehicleType(dto.vehicleType());
        tariff.setHourlyRate(dto.hourlyRate());
        tariff.setDailyRate(dto.dailyRate());
        tariff.setMonthlyRate(dto.monthlyRate());
        tariff.setValidFrom(LocalDateTime.now());
        // validUntil null means indefinite

        tariff = tariffRepository.save(tariff);
        return mapToDTO(tariff);
    }

    @Override
    public List<TariffResponseDTO> getAllTariffs() {
        return tariffRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Tariff getTariffByVehicleType(Vehicle.VehicleType type) {
        return tariffRepository.findByVehicleType(type)
                .orElseThrow(() -> new RuntimeException("Tariff not found for vehicle type: " + type));
    }

    private TariffResponseDTO mapToDTO(Tariff tariff) {
        return new TariffResponseDTO(
                tariff.getId(),
                tariff.getVehicleType(),
                tariff.getHourlyRate(),
                tariff.getDailyRate(),
                tariff.getMonthlyRate());
    }
}
