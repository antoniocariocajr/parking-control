package com.bill.parking_control.dtos.tariff;

import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import java.math.BigDecimal;
import java.time.Instant;

public record TariffResponseDTO(
        String id,
        VehicleType vehicleType,
        BigDecimal hourlyRate,
        BigDecimal dailyRate,
        BigDecimal monthlyRate,
        Instant validFrom,
        Instant validUntil,
        boolean active) {
}
