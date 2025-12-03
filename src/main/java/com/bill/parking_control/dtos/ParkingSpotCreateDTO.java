package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParkingSpotCreateDTO(
        @NotBlank String code,
        @NotNull SpotType type,
        boolean covered) {
}
