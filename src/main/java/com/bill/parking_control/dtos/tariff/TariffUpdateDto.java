package com.bill.parking_control.dtos.tariff;

import java.math.BigDecimal;
import java.time.Instant;

import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;

public record TariffUpdateDto(
        VehicleType vehicleType,
        BigDecimal hourlyRate,
        BigDecimal dailyRate,
        BigDecimal monthlyRate,
        Instant validFrom,
        Instant validUntil,
        boolean active) {

}
