package com.bill.parking_control.dtos;

import jakarta.validation.constraints.NotBlank;

public record ParkingSessionStartDTO(
        @NotBlank String vehicleLicensePlate,
        String spotCode // Optional, can be assigned automatically or manually
) {
}
