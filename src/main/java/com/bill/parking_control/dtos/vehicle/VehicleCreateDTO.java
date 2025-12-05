package com.bill.parking_control.dtos.vehicle;

import com.bill.parking_control.persistences.entities.Vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateDTO(
                @NotBlank String licensePlate,
                @NotBlank String brand,
                @NotBlank String model,
                @NotBlank String color,
                @NotNull VehicleType type,
                @NotNull @NotBlank String ownerId) {
}
