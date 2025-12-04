package com.bill.parking_control.dtos.tariff;

import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TariffCreateDTO(
        @NotNull VehicleType vehicleType,
        @NotNull BigDecimal hourlyRate,
        @NotNull BigDecimal dailyRate,
        @NotNull BigDecimal monthlyRate,
        @NotNull LocalDateTime validFrom,
        @NotNull LocalDateTime validUntil) {
}
