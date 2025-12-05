package com.bill.parking_control.dtos.session;

import java.time.Instant;

public record ParkingSessionUpdateDto(
                String vehicleLicensePlate,
                String spotCode,
                String operatorId,
                Instant entryTime) {

}
