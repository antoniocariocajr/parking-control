package com.bill.parking_control.services;

import com.bill.parking_control.dtos.tariff.TariffCreateDTO;
import com.bill.parking_control.dtos.tariff.TariffResponseDTO;
import com.bill.parking_control.persitenses.entities.Tariff;
import com.bill.parking_control.persitenses.entities.Vehicle;

import java.util.List;

public interface TariffService {
    TariffResponseDTO createTariff(TariffCreateDTO dto);

    List<TariffResponseDTO> getAllTariffs();

    Tariff getTariffByVehicleType(Vehicle.VehicleType type);
}
