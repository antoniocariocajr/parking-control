package com.bill.parking_control.dtos.reservation;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ReservationCreateDTO(
        @NotNull(message = "Client ID is required") String clientId,

        @NotNull(message = "Spot ID is required") String spotId,

        @NotNull(message = "Start time is required") @Future(message = "Start time must be in the future") LocalDateTime reservedFrom,

        @NotNull(message = "End time is required") @Future(message = "End time must be in the future") LocalDateTime reservedUntil) {
}
