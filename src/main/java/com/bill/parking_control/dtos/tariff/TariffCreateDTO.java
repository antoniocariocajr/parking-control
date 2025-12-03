package com.bill.parking_control.dtos.tariff;

import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TariffCreateDTO(
                @NotNull VehicleType vehicleType,
                @NotNull BigDecimal hourlyRate,
                BigDecimal dailyRate,
                BigDecimal monthlyRate) {
}
