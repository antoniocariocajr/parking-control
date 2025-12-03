package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateDTO(
        @NotBlank String licensePlate,
        @NotBlank String brand,
        @NotBlank String model,
        @NotBlank String color,
        @NotNull VehicleType type,
        String ownerId // Optional, can be linked later
) {
}
