package com.bill.parking_control.dtos.session;

import com.bill.parking_control.persistences.entities.ParkingSession.SessionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record ParkingSessionResponseDTO(
        String id,
        String vehicleLicensePlate,
        String spotCode,
        String operatorId,
        Instant entryTime,
        Instant exitTime,
        SessionStatus status,
        BigDecimal hourlyRate,
        BigDecimal totalAmount) {
}
