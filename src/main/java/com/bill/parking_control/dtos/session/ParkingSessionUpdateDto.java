package com.bill.parking_control.dtos.session;

import java.time.LocalDateTime;

import com.bill.parking_control.persitenses.entities.ParkingSession.SessionStatus;

public record ParkingSessionUpdateDto(
                String vehicleLicensePlate,
                String spotCode,
                String operatorId,
                LocalDateTime entryTime,
                SessionStatus status) {

}
