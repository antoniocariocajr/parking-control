package com.bill.parking_control.dtos;

import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotStatus;
import com.bill.parking_control.persitenses.entities.ParkingSpot.SpotType;

public record ParkingSpotResponseDTO(
        String id,
        String code,
        SpotType type,
        SpotStatus status,
        boolean covered) {
}
