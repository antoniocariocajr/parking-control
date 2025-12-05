package com.bill.parking_control.dtos.session;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record ParkingSessionStartDTO(
                @NotBlank String vehicleLicensePlate,
                String spotCode, // Optional, can be assigned automatically or manually
                @NotBlank String operatorId,
                Instant entryTime // Optional, can be assigned automatically or manually
) {
}
