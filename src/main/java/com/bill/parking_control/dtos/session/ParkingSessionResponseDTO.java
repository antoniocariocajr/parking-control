package com.bill.parking_control.dtos.session;

import com.bill.parking_control.persistences.entities.ParkingSession.SessionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParkingSessionResponseDTO(
                String id,
                String vehicleLicensePlate,
                String spotCode,
                String operatorId,
                LocalDateTime entryTime,
                LocalDateTime exitTime,
                SessionStatus status,
                BigDecimal hourlyRate,
                BigDecimal totalAmount) {
}
