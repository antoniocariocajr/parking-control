package com.bill.parking_control.dtos.session;

import java.time.LocalDateTime;

public record ParkingSessionUpdateDto(
        String vehicleLicensePlate,
        String spotCode,
        String operatorId,
        LocalDateTime entryTime) {

}
