package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import java.math.BigDecimal;

public record TariffResponseDTO(
        String id,
        VehicleType vehicleType,
        BigDecimal hourlyRate,
        BigDecimal dailyRate,
        BigDecimal monthlyRate) {
}
