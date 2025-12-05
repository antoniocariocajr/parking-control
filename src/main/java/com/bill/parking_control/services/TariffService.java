package com.bill.parking_control.services;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.dtos.tariff.TariffUpdateDto;
import com.bill.parking_control.persistences.entities.Vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TariffService {
    TariffResponseDTO createTariff(TariffCreateDTO dto);

    Page<TariffResponseDTO> getAllTariffs(Pageable pageable);

    TariffResponseDTO getTariffById(String id);

    TariffResponseDTO getTariffByVehicleType(Vehicle.VehicleType type);

    TariffResponseDTO updateTariff(String id, TariffUpdateDto dto);

    void deleteTariff(String id);
}
