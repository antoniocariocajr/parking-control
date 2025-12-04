package com.bill.parking_control.dtos.reservation;

import java.time.LocalDateTime;

import com.bill.parking_control.persitenses.entities.Reservation.ReservationStatus;

public record ReservationResponseDTO(
        String id,
        String clientId,
        String spotId,
        LocalDateTime reservedFrom,
        LocalDateTime reservedUntil,
        ReservationStatus status) {
}
