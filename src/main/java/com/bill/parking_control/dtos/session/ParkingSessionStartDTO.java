package com.bill.parking_control.dtos.session;

import jakarta.validation.constraints.NotBlank;

public record ParkingSessionStartDTO(
                @NotBlank String vehicleLicensePlate,
                @NotBlank String spotCode // Optional, can be assigned automatically or manually
) {
}
