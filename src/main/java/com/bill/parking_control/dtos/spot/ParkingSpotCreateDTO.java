package com.bill.parking_control.dtos.spot;

import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.Vehicle.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParkingSpotCreateDTO(
                @NotBlank String code,
                @NotNull VehicleType type,
                @NotNull SpotStatus status,
                boolean covered) {
}
