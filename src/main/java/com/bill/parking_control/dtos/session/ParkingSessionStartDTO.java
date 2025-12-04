package com.bill.parking_control.dtos.session;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record ParkingSessionStartDTO(
        @NotBlank String vehicleLicensePlate,
        String spotCode, // Optional, can be assigned automatically or manually
        @NotBlank String operatorId,
        LocalDateTime entryTime // Optional, can be assigned automatically or manually
) {
}
