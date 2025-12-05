package com.bill.parking_control.dtos.reservation;

import java.time.Instant;

import com.bill.parking_control.persistences.entities.Reservation.ReservationStatus;

public record ReservationResponseDTO(
        String id,
        String clientId,
        String spotId,
        Instant reservedFrom,
        Instant reservedUntil,
        ReservationStatus status) {
}
